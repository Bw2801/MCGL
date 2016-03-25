package de.bwueller.mcgl.condition;

import de.bwueller.mcgl.Chain;
import de.bwueller.mcgl.command.Command;
import de.bwueller.mcgl.command.CommandType;
import de.bwueller.mcgl.coordinate.Coordinates;
import de.bwueller.mcgl.nbt.NBTObject;

public class BlockExistsAtCondition extends Condition {

    private String block;
    private Coordinates coordinates;
    private NBTObject NBT;
    private int dataValue;
    
    public BlockExistsAtCondition(Coordinates location, String block, int dataValue, NBTObject NBT) {
        this.block = block;
        this.NBT = NBT == null ? new NBTObject() : NBT;
        this.dataValue = dataValue;
        this.coordinates = location;
    }
    
    public BlockExistsAtCondition(Coordinates location, String block, int dataValue) {
        this(location, block, dataValue, null);
    }
    
    public BlockExistsAtCondition(Coordinates location, String block) {
        this(location, block, -1);
    }
    
    @Override
    public void init(Chain chain) {
        chain.testForBlock(coordinates, block, dataValue, NBT);
        chain.addTag(chain.baseEntity, id, chain.baseNBT, true);
    }

    @Override
    public void dispose(Chain chain) {
        chain.simpleCommand(new Command("scoreboard players tag " + chain.baseEntity.getSelectorString() + " remove " + id + " " + chain.baseNBT.toString(), CommandType.CHAIN));
    }
}
