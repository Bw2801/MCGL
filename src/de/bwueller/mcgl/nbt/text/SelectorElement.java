package de.bwueller.mcgl.nbt.text;

import de.bwueller.mcgl.entity.Entities;
import de.bwueller.mcgl.nbt.NBTObject;

public class SelectorElement extends NBTObject {

    public SelectorElement(Entities selector) {
        set("selector", selector.toString());
    }
}
