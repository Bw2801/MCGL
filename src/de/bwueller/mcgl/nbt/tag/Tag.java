package de.bwueller.mcgl.nbt.tag;

import java.io.DataOutput;
import java.io.IOException;

public abstract class Tag {

    public enum Type {
        BYTE((byte) 1),
        SHORT((byte) 2),
        INT((byte) 3),
        LONG((byte) 4),
        FLOAT((byte) 5),
        DOUBLE((byte) 6),
        BYTE_ARRAY((byte) 7),
        STRING((byte) 8),
        LIST((byte) 9),
        COMPOUND((byte) 10),
        INT_ARRAY((byte) 11);
            
        private final byte id;
        
        private Type(byte id) {
            this.id = id;
        }
        
        public byte getId() {
            return id;
        }
    }
    
    protected final byte id;
    protected final Type type;
    protected final String name;

    protected Tag(Type type, String name) {
        this.id = type.getId();
        this.type = type;
        this.name = name;
    }
    
    public Tag(Type type) {
        this(type, "");
    }
    
    public final void write(DataOutput out) throws IOException {
        write(out, name);
    }
    
    public final void write(DataOutput out, String name) throws IOException {
        out.writeByte(id);
        out.writeUTF(name);
        writeNBT(out);
    }
    
    protected abstract void writeNBT(DataOutput out) throws IOException;
}
