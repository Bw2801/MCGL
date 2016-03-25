package de.bwueller.mcgl.condition;

import de.bwueller.mcgl.Chain;
import de.bwueller.mcgl.command.Command;
import de.bwueller.mcgl.command.CommandType;
import de.bwueller.mcgl.coordinate.Area;

public class AreasAreEqualCondition extends Condition {

    private Area source;
    private Area target;
    private String mode;
        
    public AreasAreEqualCondition(Area source, Area target, String mode) {
        this.source = source;
        this.target = target;
        this.mode = mode;
    }
    
    public AreasAreEqualCondition(Area source, Area target) {
        this(source, target, "all");
    }
        
    @Override
    public void init(Chain chain) {
        chain.testForBlocks(source, target.min, mode);
        chain.addTag(chain.baseEntity, id, chain.baseNBT, true);
    }

    @Override
    public void dispose(Chain chain) {
        chain.simpleCommand(new Command("scoreboard players tag " + chain.baseEntity.getSelectorString() + " remove " + id + " " + chain.baseNBT.toString(), CommandType.CHAIN));
    }
}
