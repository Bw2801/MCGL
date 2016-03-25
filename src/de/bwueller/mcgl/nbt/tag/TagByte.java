package de.bwueller.mcgl.nbt.tag;

import java.io.DataOutput;
import java.io.IOException;

public class TagByte extends Tag {

    private final byte value;
    
    public TagByte(byte value) {
        super(Type.BYTE);
        this.value = value;
    }

    @Override
    protected void writeNBT(DataOutput out) throws IOException {
        out.writeByte(value);
    }
}
