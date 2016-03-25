package de.bwueller.mcgl.condition;

import de.bwueller.mcgl.Chain;
import de.bwueller.mcgl.command.Command;
import de.bwueller.mcgl.command.CommandType;
import de.bwueller.mcgl.coordinate.Coordinates;
import de.bwueller.mcgl.entity.Entities;
import de.bwueller.mcgl.nbt.NBTObject;

public class EntityCanBeFoundCondition extends Condition {

    public Entities entities;
    public NBTObject NBT;

    public String detectBlock;
    public Coordinates detectCoords;
    public Integer detectDataValue;
        
    public EntityCanBeFoundCondition(Entities entities, NBTObject NBT, String detectBlock, Coordinates detectCoords, Integer detectDataValue) {
        this.entities = entities;
        this.NBT = NBT == null ? new NBTObject() : NBT;

        this.detectBlock = detectBlock;
        this.detectCoords = detectCoords;
        this.detectDataValue = detectDataValue;
    }

    public EntityCanBeFoundCondition(Entities entities, NBTObject NBT) {
        this(entities, NBT, null, null, null);
    }
    
    public EntityCanBeFoundCondition(Entities entities) {
        this(entities, null);
    }
    
    @Override
    public void init(Chain chain) {
        if (detectBlock != null)
            chain.pushExecutionAs(entities, NBT, null, detectBlock, detectCoords, detectDataValue);

        chain.testForEntity(entities, NBT);
        chain.addTag(chain.baseEntity, id, chain.baseNBT, true);

        if (detectBlock != null)
            chain.popExecution();
    }

    @Override
    public void dispose(Chain chain) {
        chain.simpleCommand(new Command("scoreboard players tag " + chain.baseEntity.getSelectorString() + " remove " + id + " " + chain.baseNBT.toString(), CommandType.CHAIN));
    }
}
