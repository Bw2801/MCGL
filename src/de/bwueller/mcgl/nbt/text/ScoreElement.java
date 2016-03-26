package de.bwueller.mcgl.nbt.text;

import de.bwueller.mcgl.entity.Entities;
import de.bwueller.mcgl.nbt.NBTObject;

public class ScoreElement extends NBTObject {

    public ScoreElement(Entities selector, String objective, TextColor color) {
        set("score", new NBTObject()
            .set("name", selector.toString())
            .set("objective", objective));
        set("color", color.toString().toLowerCase());
    }
    
    public ScoreElement(Entities selector, String objective) {
        this(selector, objective, TextColor.NONE);
    }
}
