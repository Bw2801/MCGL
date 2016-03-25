package de.bwueller.mcgl.nbt.tag;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TagList extends Tag {

    private final List<Tag> tags = new ArrayList<>();
    private final Type listType;
    
    public TagList(Type type) {
        super(Type.LIST);
        this.listType = type;
    }
    
    public void add(Tag tag) {
        if (listType != tag.type) {
            throw new IllegalArgumentException("The given tag has to be of type " + listType.toString());
        }
        
        tags.add(tag);
    }

    @Override
    protected void writeNBT(DataOutput out) throws IOException {
        if (tags.isEmpty()) {
            out.writeByte(0);
        } else {
            out.writeByte(listType.getId());
        }
        
        out.writeInt(tags.size());
        
        for (Tag tag : tags) {
            tag.writeNBT(out);
        }
    }
}
