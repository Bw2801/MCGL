package de.bwueller.mcgl.nbt.text;

import de.bwueller.mcgl.nbt.NBTArray;

public class NBTText extends NBTArray {

    private TextColor defaultColor;

    public NBTText(TextColor defaultColor) {
        this.defaultColor = defaultColor;
        add("");
    }
    
    public NBTText() {
        this(TextColor.NONE);
    }

    public NBTText add(String text, TextColor color) {
        color = color == TextColor.NONE ? defaultColor : color;
        add(new TextElement(text, color));
        return this;
    }
    
    @Override
    public NBTText add(String text) {
        return add(text, TextColor.NONE);
    }
}
