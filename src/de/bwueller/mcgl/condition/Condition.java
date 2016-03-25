package de.bwueller.mcgl.condition;

import de.bwueller.mcgl.Chain;
import de.bwueller.mcgl.Generator;

public abstract class Condition {

    public String id = Generator.getRandomString(8, "Condition");

    public abstract void init(Chain chain);
    public abstract void dispose(Chain chain);
}
