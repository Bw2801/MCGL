package de.bwueller.mcgl.nbt.text;

import de.bwueller.mcgl.entity.Entities;
import de.bwueller.mcgl.nbt.NBTObject;

public class ScoreElement extends NBTObject {

    public ScoreElement(Entities selector, String objective) {
        set("score", new NBTObject()
            .set("name", selector.toString())
            .set("objective", objective));
    }
}
