package de.bwueller.mcgl.nbt.text;

import de.bwueller.mcgl.nbt.NBTObject;

public class TextElement extends NBTObject {

    public TextElement(String text, TextColor color) {
        set("text", text);
        set("color", color.toString().toLowerCase());
    }
    
    public TextElement(String text) {
        this(text, TextColor.NONE);
    }

    public TextElement SetClickEvent(TextClickEvent clickEvent, String value) {
        set("clickEvent", new NBTObject()
            .set("action", clickEvent.toString().toLowerCase())
            .set("value", value));
        return this;
    }

    public TextElement SetHoverEvent(TextHoverEvent hoverEvent, Object value) {
        set("clickEvent", new NBTObject()
            .set("action", hoverEvent.toString().toLowerCase())
            .set("value", value.toString()));
        return this;
    }

    public TextElement SetInsertion(String value) {
        set("insertion", value);
        return this;
    }
}
