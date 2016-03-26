package de.bwueller.mcgl;

import de.bwueller.mcgl.command.Command;
import de.bwueller.mcgl.command.CommandType;
import de.bwueller.mcgl.constant.Block;
import de.bwueller.mcgl.coordinate.RelativeCoordinates;
import de.bwueller.mcgl.nbt.NBTArray;
import de.bwueller.mcgl.nbt.NBTObject;
import de.bwueller.mcgl.nbt.tag.Tag;
import de.bwueller.mcgl.nbt.tag.Tag.Type;
import de.bwueller.mcgl.nbt.tag.TagByte;
import de.bwueller.mcgl.nbt.tag.TagByteArray;
import de.bwueller.mcgl.nbt.tag.TagCompound;
import de.bwueller.mcgl.nbt.tag.TagInt;
import de.bwueller.mcgl.nbt.tag.TagList;
import de.bwueller.mcgl.nbt.tag.TagRoot;
import de.bwueller.mcgl.nbt.tag.TagShort;
import de.bwueller.mcgl.nbt.tag.TagString;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

public final class Generator {
    
    public static final Random RANDOM = new Random();
    
    public static String getRandomString(int length, String prefix) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder(length);
        sb.append("MCGL").append(prefix).append("_");
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return sb.toString();
    }
    
    public static String join(String conjunction, Object... objects) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object item : objects) {
           if (first)
              first = false;
           else
              sb.append(conjunction);
           sb.append(item.toString());
        }
        return sb.toString();
    }
    
    public static class SplitResult {
        
        public int width;
        public List<List<Command>> commands;

        public SplitResult(int width, List<List<Command>> commands) {
            this.width = width;
            this.commands = commands;
        }
    }
    
    public static class CommandblockDefinition {
        
        public int x, y, z, id;
        public int auto, data;
        public String command, type;
    }
    
    private static SplitResult splitCommands(List<Command> commands) {
        List<List<Command>> saveCommands = new ArrayList<>();
        saveCommands.add(new ArrayList<>());
        
        short current = 0;
        short currentList = 0;

        int maxWidth = 0;
        int width = 32;
        
        for (int i = 0; i < commands.size();) {
            Command command = commands.get(i);

            if (current < width) {
                saveCommands.get(currentList).add(command);
                current++;
                i++;
                maxWidth++;
            } else if (command.type == CommandType.CHAIN_CONDITIONAL) {
                width++;
            } else if (command.type == CommandType.CHAIN && commands.get(i - 1).type == CommandType.CHAIN_CONDITIONAL) {
                width++;
            } else {
                saveCommands.add(new ArrayList<>());
                current = 0;
                currentList++;
            }
        }
        
        if (maxWidth < width)
            width = maxWidth;
        
        return new SplitResult(width, saveCommands);
    }
    
    private static List<CommandblockDefinition> createCommandBlockdefinitions(SplitResult result) {
        List<CommandblockDefinition> definitions = new ArrayList<>();
        
        for (int i = 0; i < result.width * result.commands.size(); i++) {
            definitions.add(null);
        }
        
        for (int row = 0; row < result.commands.size(); row++) {
            if (result.commands.get(row).size() == result.width) {
                List<Integer> indices = new ArrayList<>();
                createCommandblockLine(definitions, row, result.commands, indices, result.width, 0);
                break;
            }
        }
        
        return definitions;
    }
    
    private static void createCommandblockLine(List<CommandblockDefinition> definitions, int row, List<List<Command>> commands, List<Integer> indices, int width, int startAt) {
        List<Command> current = new ArrayList<>(commands.get(row));
        if (row % 2 != 0)
            Collections.reverse(current);

        for (int x = 0; x < current.size(); x++) {
            int realX = startAt + x;

            Command command = current.get(x);
            CommandblockDefinition definition = new CommandblockDefinition();

            switch (command.type) {
                case IMPULSE:
                    definition.type = Block.COMMAND_BLOCK;
                    definition.id = 137;
                    break;
                case REPEAT:
                    definition.type = Block.REPEATING_COMMAND_BLOCK;
                    definition.id = 210;
                    break;
                default:
                    definition.type = Block.CHAIN_COMMAND_BLOCK;
                    definition.id = 211;
                    break;
            }

            int data = row % 2 == 0 ? 5 : 4;

            if ((data == 4 && x == 0) || (data == 5 && x == current.size() - 1))
                data = 3;

            if (command.type == CommandType.CHAIN_CONDITIONAL)
                data += 8;

            definition.data = data;
            definition.x = realX;
            definition.y = 0;
            definition.z = row;
            definition.auto = (byte) (command.type == CommandType.CHAIN || command.type == CommandType.CHAIN_CONDITIONAL ? 1 : 0);
            definition.command = command.command;

            definitions.set(row * width + realX, definition);
        }

        indices.add(row);

        if (row + 1 < commands.size() && !indices.contains(row + 1))
        {
            int index = (row % 2 == 0) ? current.size() - commands.get(row + 1).size() : startAt;
            if (current.size() == commands.get(row + 1).size())
                index = startAt;
            createCommandblockLine(definitions, row + 1, commands, indices, width, index);
        }

        if (row - 1 >= 0 && !indices.contains(row - 1))
        {
            int index = (row % 2 == 0) ? startAt : current.size() - commands.get(row - 1).size();
            if (current.size() == commands.get(row - 1).size())
                index = startAt;
            createCommandblockLine(definitions, row - 1, commands, indices, width, index);
        }
    }
    
    private static List<String> createSetblockCommands(List<CommandblockDefinition> definitions, boolean trackOutput) {
        List<String> commands = new ArrayList<>();
                
        int yCount = 0;
        for (CommandblockDefinition definition : definitions) {
            if (definition == null) continue;
            
            int x = 2 + definition.x;
            int y = -(2 + yCount);
            int z = definition.z;
            
            yCount++;
            
            NBTObject NBT = new NBTObject()
                    .set("id", "Control")
                    .set("x", definition.x)
                    .set("y", definition.y)
                    .set("z", definition.z)
                    .set("auto", definition.auto)
                    .set("Command", escapeQuotes(definition.command))
                    .set("TrackOutput", trackOutput)
                    .set("SuccessCount", 0);
            
            if (z > 0) {
                Integer a = null;
            }
            
            commands.add("setblock " + new RelativeCoordinates(x, y, z).toString() + " " + definition.type + " " + definition.data + " replace " + NBT.toString());
        }
        
        return commands;
    }
    
    private static String escapeQuotes(String myString) {
        String replaced = myString;
        
        replaced = replaced.replaceAll(Pattern.quote("\\\""), Matcher.quoteReplacement("\\\\\""));
        replaced = replaced.replaceAll(Pattern.quote("\""), Matcher.quoteReplacement("\\\""));
        
        return replaced;
    }
    
    private static List<String> generateSingleCommands(List<CommandblockDefinition> definitions, int width, int maxHeight, boolean trackOutput) {
        List<String> commands = new ArrayList<>();
        
        int rows = (int) Math.ceil(definitions.size() / (width * 1.0));
        int divider = 1;
        int generate;
        int part;
        
        do {
            part = rows * width / divider;
            generate = divider;
            commands.clear();
            for (int i = 0; i < divider; i++) {
                List<String> setblockCommands = createSetblockCommands(definitions.subList(i * part, i * part + part), trackOutput);
                String command = generateSingleCommand(setblockCommands);
                                
                if (command.length() >= 32767 || setblockCommands.size() > maxHeight) {
                    divider *= 2;
                    break;
                } else {
                    generate--;
                    commands.add(command);
                }
            }
        } while (generate != 0 && divider <= rows);
        
        return commands;
    }
    
    public static List<String> getOneClickCommands(List<Command> commands, int maxHeight, boolean trackOutput) {
        SplitResult splitResult = splitCommands(commands);
        List<CommandblockDefinition> definitions = createCommandBlockdefinitions(splitResult);
        List<String> result = generateSingleCommands(definitions, splitResult.width, maxHeight, trackOutput);
        
        if (result.isEmpty()) {
            throw new AssertionError("Unable to generate commands shorter than 32767 characters.");
        }
        
        return result;
    }
    
    public static void generateOneClickCommands(List<Command> commands, int maxHeight, boolean trackOutput, boolean copyWindow) {
        List<String> result = getOneClickCommands(commands, maxHeight, trackOutput);
        
        if (maxHeight > commands.size() + 3) {
            maxHeight = commands.size() + 3;
        }
        
        if (copyWindow) {
            new OneClickFrame(result);
        } else {
            System.out.println("!! MAKE SURE THERE ARE LEAST " + maxHeight + " BLOCKS OF AIR ABOVE THE COMMAND BLOCK !!");
            if (result.size() > 1) System.out.println("There are multiple commands, because a single one was too long for a command block.");
            System.out.println("1. Copy the top most command and paste it in a command block.");
            System.out.println("2. Power the command block and wait until the commandblocks have been spawned.");
            if (result.size() > 1) System.out.println("3. Do the same for all following commands with the same command block.");
            System.out.println("---");
            result.stream().forEach((command) -> {
                System.out.println(command);
            });
            System.out.println("---");
        }
    }
    
    public static void generateOneClickCommands(List<Command> commands, int maxHeight, boolean trackOutput) {
        generateOneClickCommands(commands, maxHeight, trackOutput, false);
    }
    
    public static void generateOneClickCommands(List<Command> commands, int maxHeight) {
        generateOneClickCommands(commands, maxHeight, false, false);
    }
    
    public static void generateOneClickCommands(List<Command> commands) {
        generateOneClickCommands(commands, 64);
    }
    
    public static String generateSingleCommand(List<String> commands) {
        commands.add("fill ~-1 ~-" + (commands.size() + 1) + " ~ ~ ~2 ~ air");
        commands.add("fill ~-1 ~-" + (commands.size() + 1) + " ~ ~-1 ~ ~ redstone_block");
        
        NBTObject nbt = new NBTObject().set("Block", "stone").set("Time", 1);
        NBTObject current = nbt;
        
        for (int i = 0; i < commands.size(); i++) {
            NBTObject cmd = new NBTObject().set("Command", escapeQuotes(commands.get(i)));
            NBTObject extra = new NBTObject().set("id", "FallingSand").set("Block", "command_block").set("TileEntityData", cmd).set("Time", 1);
            current.set("Passengers", new NBTArray().add(extra));
            current = extra;
        }
        
        NBTObject extra = new NBTObject().set("id", "FallingSand").set("Block", "redstone_block").set("Time", 1);
        current.set("Passengers", new NBTArray().add(extra));
        
        return "summon FallingSand ~ ~1 ~ " + nbt.toString();
    }
    
    public static void generateSchematics(List<Command> commands, String path) {
        generateSchematics(commands, path, false);
    }
    
    public static void generateSchematics(List<Command> commands, String path, boolean trackOutput) {
        TagRoot root = new TagRoot("Schematic");
        
        SplitResult splitResult = splitCommands(commands);
        List<CommandblockDefinition> definitions = createCommandBlockdefinitions(splitResult);
        
        short width = (short) splitResult.width;
        short height = (short) 1;
        short length = (short) splitResult.commands.size();
        
        root.set("Width", new TagShort(width));
        root.set("Height", new TagShort(height));
        root.set("Length", new TagShort(length));
        
        byte[] blockIds = new byte[width * height * length];
        byte[] blockData = new byte[width * height * length];
        
        TagList tileEntities = new TagList(Type.COMPOUND);
        
        for (int i = 0; i < definitions.size(); i++) {
            CommandblockDefinition definition = definitions.get(i);
            
            if (definition == null) {
                definition = new CommandblockDefinition();
                definition.command = "";
                definition.type = "";
            }
            
            TagCompound tileEntity = new TagCompound();
            
            tileEntity.set("id", new TagString("Control"));
            tileEntity.set("CustomName", new TagString("@"));
            
            tileEntity.set("x", new TagInt(definition.x));
            tileEntity.set("y", new TagInt(definition.y));
            tileEntity.set("z", new TagInt(definition.z));
            
            tileEntity.set("auto", new TagByte((byte) definition.auto));
            
            tileEntity.set("Command", new TagString(definition.command));
            tileEntity.set("TrackOutput", new TagByte((byte) (trackOutput ? 1 : 0)));
            tileEntity.set("SuccessCount", new TagInt(0));
            
            tileEntities.add(tileEntity);
            
            blockIds[i] = (byte) definition.id;
            blockData[i] = (byte) definition.data;
        }
        
        root.set("Blocks", new TagByteArray(blockIds));
        root.set("Data", new TagByteArray(blockData));
        root.set("Entities", new TagList(Type.COMPOUND));
        root.set("TileEntities", tileEntities);
        
        try {
            File output = new File(path);
            
            if (output.exists()) output.delete();
            output.createNewFile();
            
            DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(output)));
            
            root.write(stream);
            stream.close();
            
            System.out.println("Schematic file has been generated: " + output.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
