package de.bwueller.mcgl.nbt.tag;

import java.io.DataOutput;
import java.io.IOException;

public class TagString extends Tag {

    private final String value;
    
    public TagString(String value) {
        super(Type.STRING);
        this.value = value;
    }

    @Override
    protected void writeNBT(DataOutput out) throws IOException {
        out.writeUTF(value);
    }
}
