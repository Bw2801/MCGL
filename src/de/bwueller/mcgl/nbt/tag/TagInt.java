package de.bwueller.mcgl.nbt.tag;

import java.io.DataOutput;
import java.io.IOException;

public class TagInt extends Tag {

    private final int value;
    
    public TagInt(int value) {
        super(Tag.Type.INT);
        this.value = value;
    }

    @Override
    protected void writeNBT(DataOutput out) throws IOException {
        out.writeInt(value);
    }
}
