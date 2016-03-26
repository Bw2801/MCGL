package de.bwueller.mcgl.condition;

import de.bwueller.mcgl.Chain;
import de.bwueller.mcgl.command.Command;
import de.bwueller.mcgl.command.CommandType;

public class WasSuccessfulCondition extends Condition {

    @Override
    public void init(Chain chain) {
        chain.addTag(chain.baseEntity, id, null, true);
    }

    @Override
    public void dispose(Chain chain) {
        chain.simpleCommand(new Command("scoreboard players tag " + chain.baseEntity.getSelectorString() + " remove " + id + " " + chain.baseNBT.toString(), CommandType.CHAIN));
    }

}
