package de.bwueller.mcgl.nbt.tag;

import java.io.DataOutput;
import java.io.IOException;

public class TagShort extends Tag {

    private final short value;
    
    public TagShort(short value) {
        super(Type.SHORT);
        this.value = value;
    }

    @Override
    protected void writeNBT(DataOutput out) throws IOException {
        out.writeShort(value);
    }
}
