package de.bwueller.mcgl;

import de.bwueller.mcgl.command.*;
import de.bwueller.mcgl.condition.*;
import de.bwueller.mcgl.coordinate.*;
import de.bwueller.mcgl.entity.*;
import de.bwueller.mcgl.nbt.*;
import de.bwueller.mcgl.nbt.text.NBTText;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Chain extends CommandHandler {
    
    private List<Command> commands = new ArrayList<>();
    
    private final Stack<Condition> conditions = new Stack<>();
    private final Stack<Condition> executions = new Stack<>();
    
    public Entities baseEntity;
    public NBTObject baseNBT;
    
    private Entities currentExecution;
    private Entities currentExecutionOriginal;
    private NBTObject currentExecutionNBT;
    
    private final Stack<Coordinates> executionCoordinates = new Stack<>();
    private final Stack<String> detectStrings = new Stack<>();
    
    private boolean ignore = false;
    private int singleExecutions = 0;
    private int singleConditions = 0;

    private CommandType base;
    private CommandType next = CommandType.CHAIN;

    private boolean baseEntityRequired = false;

    public Chain(CommandType type, boolean hideBaseEntity) {
        base = type;
        
        if (base != CommandType.IMPULSE && base != CommandType.REPEAT) {
            throw new IllegalArgumentException("The chain type has to be either IMPULSE or REPEAT.");
        }
        
        String id = Generator.getRandomString(8, "Chain");

        baseEntity = Entities.getSingle(EntityType.ArmorStand).withName(id);
        baseNBT = new NBTObject().set("CustomName", id);
        if (hideBaseEntity)
            baseNBT = baseNBT.set("Invisible", true);
    }
    
    public Chain(CommandType type) {
        this(type, false);
    }
    
    public Chain() {
        this(CommandType.IMPULSE);
    }
    
    public List<Command> getCommands() {
        List<Command> oldCommands = new ArrayList<>(commands);
        commands = new ArrayList<>();
        
        List<Command> newCommands = new ArrayList<>(oldCommands);
        
        if (baseEntityRequired) {
            CommandType oldNext = next;
            next = base;
            summonEntity(EntityType.ArmorStand, new RelativeCoordinates(0, 0, 0), baseNBT);
            next = CommandType.CHAIN;
            kill(baseEntity);
            next = oldNext;

            newCommands.add(0, commands.get(0));
            newCommands.add(newCommands.size(), commands.get(1));
        } else {
            newCommands.set(0, new Command(newCommands.get(0).command, base));
        }
        
        commands = oldCommands;
        
        return newCommands;
    }
    
    public void ignore() {
        ignore = true;
    }

    public void stopIgnore() {
        ignore = false;
    }

    public void pushExecutionAs(Entities entities, NBTObject NBT, Coordinates coordinates, String detectBlock, Coordinates detectCoordinates, Integer detectDataValue)
    {
        String detect = null;

        if (detectBlock != null && (detectCoordinates == null || detectDataValue == null))
            throw new NullPointerException("All of the detect parameters have to be set.");

        if (detectBlock != null)
            detect = "detect " + detectCoordinates.toString() + " " + detectBlock + " " + detectDataValue;

        if (coordinates == null)
            coordinates = RelativeCoordinates.Zero;

        if (NBT == null)
            NBT = new NBTObject();

        if (currentExecution != null) {
            baseEntityRequired = true;
            removeTag(currentExecutionOriginal, "MCGL_EXECUTING");
            currentExecution = null;
            Condition cond = new EntityCanBeFoundCondition(currentExecutionOriginal, currentExecutionNBT);
            cond.init(this);
            executions.push(cond);
        }

        addTag(entities, "MCGL_EXECUTING", NBT);
        currentExecutionOriginal = entities;
        currentExecution = entities.withTag("MCGL_EXECUTING");
        currentExecutionNBT = NBT;
        executionCoordinates.push(coordinates);
        detectStrings.push(detect);
    }
    
    public void pushExecutionAs(Entities entities, NBTObject NBT, Coordinates coordinates) {
        pushExecutionAs(entities, NBT, coordinates, null, null, null);
    }
    
    public void pushExecutionAs(Entities entities, NBTObject NBT) {
        pushExecutionAs(entities, NBT, null);
    }
    
    public void pushExecutionAs(Entities entities) {
        pushExecutionAs(entities, null);
    }

    public void popExecution() {
        currentExecution = null;
        removeTag(currentExecutionOriginal, "MCGL_EXECUTING", currentExecutionNBT);

        if (executions.size() > 0)
        {
            EntityCanBeFoundCondition cond = (EntityCanBeFoundCondition) executions.pop();
            addTag(currentExecutionOriginal, "MCGL_EXECUTING", cond.NBT);
            cond.dispose(this);
            currentExecutionOriginal = cond.entities;
            currentExecution = cond.entities.withTag("MCGL_EXECUTING");
        }

        executionCoordinates.pop();
        detectStrings.pop();
    }

    public Chain executeAs(Entities entities, NBTObject NBT, Coordinates coordinates, String detectBlock, Coordinates detectCoordinates, Integer detectDataValue) {
        pushExecutionAs(entities, NBT, coordinates, detectBlock, detectCoordinates, detectDataValue);
        singleExecutions++;
        return this;
    }
    
    public Chain executeAs(Entities entities, NBTObject NBT, Coordinates coordinates) {
        return executeAs(entities, NBT, coordinates, null, null, null);
    }
    
    public Chain executeAs(Entities entities, NBTObject NBT) {
        return executeAs(entities, NBT, null);
    }
    
    public Chain executeAs(Entities entities) {
        return executeAs(entities, null);
    }

    public Chain ifCondition(Condition condition) {
        pushCondition(condition);
        singleConditions++;
        return this;
    }

    public void pushCondition(Condition condition) {
        condition.init(this);
        conditions.push(condition);
        baseEntityRequired = true;
    }

    public void popCondition() {
        conditions.pop().dispose(this);
    }
    
    public EntityCanBeFoundCondition entityCanBeFound(Entities entities, NBTObject NBT, String detectBlock, Coordinates detectCoords, Integer detectDataValue) {
        return new EntityCanBeFoundCondition(entities, NBT, detectBlock, detectCoords, detectDataValue);
    }
    
    public EntityCanBeFoundCondition entityCanBeFound(Entities entities, NBTObject NBT) {
        return entityCanBeFound(entities, NBT, null, null, null);
    }
    
    public EntityCanBeFoundCondition entityCanBeFound(Entities entities) {
        return entityCanBeFound(entities, null);
    }
    
    public BlockExistsAtCondition blockExistsAt(Coordinates location, String block, int dataValue, NBTObject NBT) {
        return new BlockExistsAtCondition(location, block, dataValue, NBT);
    }
    
    public BlockExistsAtCondition blockExistsAt(Coordinates location, String block, int dataValue) {
        return blockExistsAt(location, block, dataValue, null);
    }
    
    public BlockExistsAtCondition blockExistsAt(Coordinates location, String block) {
        return blockExistsAt(location, block, -1);
    }
    
    public AreasAreEqualCondition areasAreEqual(Area source, Area target, String mode) {
        return new AreasAreEqualCondition(source, target, mode);
    }
    
    public AreasAreEqualCondition areasAreEqual(Area source, Area target) {
        return areasAreEqual(source, target, null);
    }

    private String getConditionString() {
        String result = "";
        for (Condition condition : conditions) {
            result += "execute " + baseEntity.withTag(condition.id).getSelectorString() + " ~ ~ ~ ";
        }
        for (Condition condition : executions) {
            result += "execute " + baseEntity.withTag(condition.id).getSelectorString() + " ~ ~ ~ ";
        }
        return result;
    }

    public String getExecutionString() {
        String result = getConditionString();
        if (currentExecution != null)
        {
            String detect = detectStrings.peek();
            result += "execute " + currentExecution.getSelectorString() + " " + executionCoordinates.peek().toString() + " " + (detect == null ? "" : detect + " ");
        }
        return result;
    }
    
    private CommandType getCurrentCommandType(boolean ifPrevious) {
        return ifPrevious ? CommandType.CHAIN_CONDITIONAL : next;
    }
    
    public void entityCommand(Command command, Entities entities) {
        String cmd = getExecutionString();
        if (cmd.endsWith(entities.getSelectorString() + " ~ ~ ~ "))
            cmd = getConditionString();
        cmd += command.command;
        simpleCommand((new Command(cmd, command.type)));
    }
    
    public void combineCommand(Command command) {
        simpleCommand(new Command((ignore ? "" : getExecutionString()) + command.command, command.type));
    }
    
    public void simpleCommand(Command command) {
        commands.add(command);
        next = CommandType.CHAIN;

        if (singleConditions > 0) {
            singleConditions--;
            popCondition();
        }

        if (singleExecutions > 0) {
            singleExecutions--;
            popExecution();
        }
    }
    
    @Override
    public void customCommand(String command, boolean ifPrevious) {
        combineCommand(new Command(command, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void giveAchievement(Entities players, String achievement, boolean ifPrevious) {
        entityCommand(new Command("achievement give " + achievement + " " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void takeAchievement(Entities players, String achievement, boolean ifPrevious) {
        entityCommand(new Command("achievement take " + achievement + " " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void setBlockData(Coordinates coordinates, NBTObject NBT, boolean ifPrevious) {
        combineCommand(new Command("blockdata " + coordinates.toString() + " " + NBT.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setEntityData(Entities entities, NBTObject NBT, boolean ifPrevious) {
        entityCommand(new Command("entitydata " + entities.getSelectorString() + " " + NBT.toString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void setBlockStat(Coordinates location, String stat, Entities targets, String objective, boolean ifPrevious) {
        combineCommand(new Command("stats block " + location.toString() + " set " + stat + " " + targets.getSelectorString() + " " + objective, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setEntityStat(Entities entities, String stat, Entities targets, String objective, boolean ifPrevious) {
        combineCommand(new Command("stats entity " + entities.getSelectorString() + " set " + stat + " " + targets.getSelectorString() + " " + objective, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void clearBlockStat(Coordinates location, String stat, boolean ifPrevious) {
        combineCommand(new Command("stats block " + location.toString() + " clear " + stat, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void clearEntityStat(Entities entities, String stat, boolean ifPrevious) {
        combineCommand(new Command("stats entity " + entities.getSelectorString() + " clear " + stat, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void clearInventory(Entities players, boolean ifPrevious) {
        entityCommand(new Command("clear " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void clearItemFromInventory(Entities players, String item, int dataValue, int amount, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        entityCommand(new Command("clear " + players.getSelectorString() + " " + item + " " + dataValue + " " + amount + " " + NBT.toString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void giveItem(Entities players, String item, int amount, int dataValue, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        entityCommand(new Command("give " + players.getSelectorString() + " " + item + " " + amount + " " + dataValue + " " + NBT.toString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void replaceBlockItem(Coordinates location, String slot, String item, int amount, int dataValue, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        combineCommand(new Command("replaceitem block " + location.toString() + " " + slot + " " + item + " " + amount + " " + dataValue + " " + NBT.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void replaceEntityItem(Entities entities, String slot, String item, int amount, int dataValue, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        combineCommand(new Command("replaceitem entity " + entities.getSelectorString() + " " + slot + " " + item + " " + amount + " " + dataValue + " " + NBT.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setDefaultGamemode(String gamemode, boolean ifPrevious) {
        combineCommand(new Command("defaultgamemode " + gamemode, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setGamemode(Entities players, String gamemode, boolean ifPrevious) {
        entityCommand(new Command("gamemode " + gamemode + " " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void setDifficulty(String difficulty, boolean ifPrevious) {
        combineCommand(new Command("difficulty " + difficulty, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setGamerule(String gamerule, Object value, boolean ifPrevious) {
        combineCommand(new Command("gamerule " + gamerule + " " + value.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void giveEffect(Entities entities, String effect, int duration, int amplifier, boolean hideParticles, boolean ifPrevious) {
        entityCommand(new Command("effect " + entities.getSelectorString() + " " + effect + " " + duration + " " + amplifier + " " + hideParticles, getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void takeEffect(Entities entities, String effect, boolean ifPrevious) {
        giveEffect(entities, effect, 0, 0, true, ifPrevious);
    }

    @Override
    public void clearEffects(Entities entities, boolean ifPrevious) {
        entityCommand(new Command("effect " + entities.getSelectorString() + " clear", getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void enchant(Entities players, String enchantment, int level, boolean ifPrevious) {
        if (level < 1)
            throw new IllegalArgumentException("Level must be greater than zero.");
        entityCommand(new Command("enchant " + players.getSelectorString() + " " + enchantment + " " + level, getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void cloneArea(Area source, Coordinates target, String mask, String mode, String block, boolean ifPrevious) {
        if ("filtered".equals(mask) && block == null)
            throw new IllegalArgumentException("Block has to be specified in order to use the \"filtered\" mask mode.");
        combineCommand(new Command("clone " + source.toString() + " " + target.toString() + " " + mask + " " + mode + (block != null ? " " + block : ""), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void fillArea(Area area, String block, int dataValue, String mode, NBTObject NBT, boolean ifPrevious) {
        if ("replace".equals(mode))
            throw new IllegalArgumentException("Cannot use mode \"replace\". Use ReplaceArea() instead.");
        if (NBT == null)
            NBT = new NBTObject();
        combineCommand(new Command("fill " + area.toString() + " " + block + " " + dataValue + " " + mode + " " + NBT.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void replaceArea(Area area, String block, int dataValue, String replaceWith, int replaceWithDataValue, boolean ifPrevious) {
        if (replaceWith == null)
            combineCommand(new Command("fill " + area.toString() + " " + block + " " + dataValue + " replace", getCurrentCommandType(ifPrevious)));
        else
            combineCommand(new Command("fill " + area.toString() + " " + block + " " + dataValue + " replace " + replaceWith + " " + replaceWithDataValue, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setBlock(Coordinates location, String block, int dataValue, String mode, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        combineCommand(new Command("setblock " + location.toString() + " " + block + " " + dataValue + " " + mode + " " + NBT.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void kill(Entities entities, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            entityCommand(new Command("kill " + entities.getSelectorString(), getCurrentCommandType(ifPrevious)), entities);
        else
        {
            addTag(entities, "MCGL_KILL", NBT);
            kill(entities.withTag("MCGL_KILL"));
        }
    }

    @Override
    public void sendStatusMessage(String message, boolean ifPrevious) {
        combineCommand(new Command("me " + message, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void sendChatMessage(String message, boolean ifPrevious) {
        combineCommand(new Command("say " + message, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void sendPrivateMessage(Entities players, String message, boolean ifPrevious) {
        combineCommand(new Command("tell " + players.getSelectorString() + " " + message, getCurrentCommandType(ifPrevious)));
    }
    
    @Override
    public void sendRawChatMessage(Entities players, NBTText message, boolean ifPrevious) {
        entityCommand(new Command("tellraw " + players.getSelectorString() + " " + message.toString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void showParticles(String particle, Coordinates location, AbsoluteCoordinates distance, double speed, int count, String mode, Entities players, String parameters, boolean ifPrevious) {
        if (players == null)
            combineCommand(new Command("particle " + particle + " " + location.toString() + " " + distance.toString() + " " + speed + " " + count + " " + mode, getCurrentCommandType(ifPrevious)));
        else
            combineCommand(new Command("particle " + particle + " " + location.toString() + " " + distance.toString() + " " + speed + " " + count + " " + mode + " " + players.getSelectorString() + " " + parameters, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void playSound(String sound, String source, Entities players, Coordinates location, double volume, double pitch, double minimumVolume, boolean ifPrevious) {
        if (location == null)
            entityCommand(new Command("playsound " + sound + " " + source + " " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
        else
            entityCommand(new Command("playsound " + sound + " " + source + " " + players.getSelectorString() + " " + location.toString() + " " + volume + " " + pitch + " " + minimumVolume, getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void setGlobalSpawn(Coordinates location, boolean ifPrevious) {
        combineCommand(new Command("setworldspawn " + location.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setSpawnPoint(Entities players, Coordinates location, boolean ifPrevious) {
        if (location == null)
            entityCommand(new Command("spawnpoint " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
        else
            entityCommand(new Command("spawnpoint " + players.getSelectorString() + " " + location.toString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void spreadEntities(Entities entities, Coordinates center, double targetDistance, double range, boolean respectTeams, boolean ifPrevious) {
        entityCommand(new Command("spreadplayers " + center.to2DString() + " " + targetDistance + " " + range + " " + respectTeams + " " + entities.getSelectorString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void summonEntity(EntityType type, Coordinates location, NBTObject NBT, boolean ifPrevious) {
        if (type == EntityType.ANY)
            throw new IllegalArgumentException("Cannot summon an entity with type ANY.");
        if (NBT == null)
            NBT = new NBTObject();
        combineCommand(new Command("summon " + type.toString() + " " + location.toString() + " " + NBT.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void testForEntity(Entities entities, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        combineCommand(new Command("testfor " + entities.getSelectorString() + " " + NBT.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void testForBlock(Coordinates location, String block, int dataValue, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        combineCommand(new Command("testforblock " + location.toString() + " " + block + " " + dataValue + " " + NBT.toString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void testForBlocks(Area source, Coordinates target, String mode, boolean ifPrevious) {
        combineCommand(new Command("testforblocks " + source.toString() + " " + target.toString() + " " + mode, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setTime(int value, boolean ifPrevious) {
        combineCommand(new Command("time set " + value, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void addTime(int value, boolean ifPrevious) {
        combineCommand(new Command("time add " + value, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void queryTime(String value, boolean ifPrevious) {
        combineCommand(new Command("time add " + value, getCurrentCommandType(ifPrevious)));
    }
    
    @Override
    public void toggleDownfall(boolean ifPrevious) {
        combineCommand(new Command("toggledownfall", getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void teleportToLocation(Entities entities, Coordinates target, Rotation rotation, boolean ifPrevious) {
        entityCommand(new Command("tp " + entities.getSelectorString() + " " + target.toString() + (rotation == null ? "" : " " + rotation.toString()), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void teleportToEntity(Entities entities, Entities target, boolean ifPrevious) {
        combineCommand(new Command("tp " + entities.getSelectorString() + " " + target.getSelectorString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void addToTrigger(String objective, int value, boolean ifPrevious) {
        combineCommand(new Command("trigger " + objective + " add " + value, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setTrigger(String objective, int value, boolean ifPrevious) {
        combineCommand(new Command("trigger " + objective + " set " + value, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setWeather(String weather, int duration, boolean ifPrevious) {
        combineCommand(new Command("weather " + weather + " " + duration, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void addExperience(Entities players, int amount, boolean ifPrevious) {
        entityCommand(new Command("xp " + amount + " " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void addExperienceLevels(Entities players, int amount, boolean ifPrevious) {
        entityCommand(new Command("xp " + amount + "L " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void removeExperience(Entities players, boolean ifPrevious) {
        entityCommand(new Command("xp -2147483648L " + players.getSelectorString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void increaseWorldBorder(int distance, int time, boolean ifPrevious) {
        combineCommand(new Command("worldborder add " + distance + " " + time, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setWorldBorder(int distance, int time, boolean ifPrevious) {
        combineCommand(new Command("worldborder set " + distance + " " + time, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setWorldBorderCenter(Coordinates center, boolean ifPrevious) {
        combineCommand(new Command("worldborder center " + center.to2DString(), getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setWorldBorderDamageRate(int damagePerBlock, boolean ifPrevious) {
        combineCommand(new Command("worldborder damage amount " + damagePerBlock, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setWorldBorderDamageBuffer(int distance, boolean ifPrevious) {
        combineCommand(new Command("worldborder damage buffer " + distance, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setWorldBorderWarningDistance(int distance, boolean ifPrevious) {
        combineCommand(new Command("worldborder warning distance " + distance, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setWorldBorderWarningTime(int time, boolean ifPrevious) {
        combineCommand(new Command("worldborder warning time " + time, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void clearTitle(Entities players, boolean ifPrevious) {
        entityCommand(new Command("title " + players.getSelectorString() + " clear", getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void resetTitle(Entities players, boolean ifPrevious) {
        entityCommand(new Command("title " + players.getSelectorString() + " reset", getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void setTitleTimes(Entities players, int fadeIn, int show, int fadeOut, boolean ifPrevious) {
        entityCommand(new Command("title " + players.getSelectorString() + " times " + fadeIn + " " + show + " " + fadeOut, getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void setSubtitle(Entities players, NBTText title, boolean ifPrevious) {
        entityCommand(new Command("title " + players.getSelectorString() + " subtitle " + title.toString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void showTitle(Entities players, NBTText title, boolean ifPrevious) {
        entityCommand(new Command("title " + players.getSelectorString() + " title " + title.toString(), getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void addScoreboardObjective(String objective, String criteria, String displayName, boolean ifPrevious) {
        combineCommand(new Command("scoreboard objectives add " + objective + " " + criteria + " " + displayName, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void removeScoreboardObjective(String objective, boolean ifPrevious) {
        combineCommand(new Command("scoreboard objectives remove " + objective, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void showScoreboardObjective(String objective, String slot, boolean ifPrevious) {
        combineCommand(new Command("scoreboard objectives setdisplay " + slot + " " + objective, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void hideScoreboard(String slot, boolean ifPrevious) {
        combineCommand(new Command("scoreboard objectives setdisplay " + slot, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void setScore(Entities entities, String objective, int score, NBTObject NBT, boolean ifPrevious) {
        entityCommand(new Command("scoreboard players set " + entities.getSelectorString() + " " + objective + " " + score + " " + NBT.toString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void addToScore(Entities entities, String objective, int amount, NBTObject NBT, boolean ifPrevious) {
        entityCommand(new Command("scoreboard players add " + entities.getSelectorString() + " " + objective + " " + amount + " " + NBT.toString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void removeFromScore(Entities entities, String objective, int amount, NBTObject NBT, boolean ifPrevious) {
        entityCommand(new Command("scoreboard players remove " + entities.getSelectorString() + " " + objective + " " + amount + " " + NBT.toString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void resetScore(Entities entities, String objective, boolean ifPrevious) {
        entityCommand(new Command("scoreboard players reset " + entities.getSelectorString() + " " + objective, getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void enableTrigger(Entities players, String trigger, boolean ifPrevious) {
        entityCommand(new Command("scoreboard players enable " + players.getSelectorString() + " " + trigger, getCurrentCommandType(ifPrevious)), players);
    }

    @Override
    public void testForScore(Entities entities, String objective, int min, int max, boolean ifPrevious) {
        entityCommand(new Command("scoreboard players test " + entities.getSelectorString() + " " + objective + " " + min + " " + max, getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void modifyScore(Entities target, String targetObjective, String operation, Entities sourceEntities, String sourceObjective, boolean ifPrevious) {
        if (sourceObjective == null)
            sourceObjective = targetObjective;
        combineCommand(new Command("scoreboard players operation " + target.getSelectorString() + " " + targetObjective + " " + operation + " " + sourceEntities.getSelectorString() + " " + sourceObjective, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void addTeam(String name, String displayName, boolean ifPrevious) {
        combineCommand(new Command("scoreboard teams add " + name + " " + displayName, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void removeTeam(String team, boolean ifPrevious) {
        combineCommand(new Command("scoreboard teams remove " + team, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void emptyTeam(String team, boolean ifPrevious) {
        combineCommand(new Command("scoreboard teams empty " + team, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void addEntityToTeam(Entities entities, String team, boolean ifPrevious) {
        entityCommand(new Command("scoreboard teams join " + team + entities.getSelectorString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void removeEntityFromTeam(Entities entities, String team, boolean ifPrevious) {
        entityCommand(new Command("scoreboard teams leave " + team + entities.getSelectorString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void removeEntityFromAllTeams(Entities entities, boolean ifPrevious) {
        entityCommand(new Command("scoreboard teams leave " + entities.getSelectorString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void setTeamOption(String team, String option, String value, boolean ifPrevious) {
        combineCommand(new Command("scoreboard teams option " + team + " " + option + " " + value, getCurrentCommandType(ifPrevious)));
    }

    @Override
    public void addTag(Entities entities, String tag, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        entityCommand(new Command("scoreboard players tag " + entities.getSelectorString() + " add " + tag + " " + NBT.toString(), getCurrentCommandType(ifPrevious)), entities);
    }

    @Override
    public void removeTag(Entities entities, String tag, NBTObject NBT, boolean ifPrevious) {
        if (NBT == null)
            NBT = new NBTObject();
        entityCommand(new Command("scoreboard players tag " + entities.getSelectorString() + " remove " + tag + " " + NBT.toString(), getCurrentCommandType(ifPrevious)), entities);
    }
}
