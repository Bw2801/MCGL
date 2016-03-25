package de.bwueller.mcgl.nbt.tag;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TagCompound extends Tag {

    private Map<String, Tag> tags = new HashMap<>();
    
    protected TagCompound(String name) {
        super(Type.COMPOUND, name);
    }
    
    public TagCompound() {
        this("");
    }
    
    public void set(String name, Tag tag) {
        tags.put(name, tag);
    }

    @Override
    protected void writeNBT(DataOutput out) throws IOException {
        for (String key : tags.keySet()) {
            Tag tag = tags.get(key);
            
            if (tag.id != 0) {
                tags.get(key).write(out, key);
            } else {
                tags.get(key).write(out);
            }
        }
        out.writeByte(0);
    }
}
