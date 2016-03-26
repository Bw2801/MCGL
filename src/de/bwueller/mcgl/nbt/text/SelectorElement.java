package de.bwueller.mcgl.nbt.text;

import de.bwueller.mcgl.entity.Entities;
import de.bwueller.mcgl.nbt.NBTObject;

public class SelectorElement extends NBTObject {

    public SelectorElement(Entities selector, TextColor color) {
        set("selector", selector.toString());
        set("color", color.toString().toLowerCase());
    }
    
    public SelectorElement(Entities selector) {
        this(selector, TextColor.NONE);
    }
}
