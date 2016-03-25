package de.bwueller.mcgl.nbt.tag;

import java.io.DataOutput;
import java.io.IOException;

public class TagByteArray extends Tag {

    private final byte[] value;
    
    public TagByteArray(byte[] value) {
        super(Type.BYTE_ARRAY);
        this.value = value;
    }

    @Override
    protected void writeNBT(DataOutput out) throws IOException {
        out.writeInt(value.length);
        out.write(value);
    }

}
