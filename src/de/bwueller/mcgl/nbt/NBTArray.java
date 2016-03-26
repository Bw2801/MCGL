package de.bwueller.mcgl.nbt;

import de.bwueller.mcgl.Generator;
import java.util.ArrayList;
import java.util.List;

public class NBTArray {

    private final List<Object> data = new ArrayList<>();

    public NBTArray() {
    }
    
    public NBTArray(String json) {
        json = json.substring(1, json.length() - 2);
        
        String[] split = json.split(",");
        for (String part : split) {
            addCustom(part);
        }
    }
    
    public NBTArray add(String value)
    {
        data.add("\"" + value + "\"");
        return this;
    }

    public NBTArray add(int value)
    {
        data.add(value + "s");
        return this;
    }

    public NBTArray add(double value)
    {
        data.add(value + "d");
        return this;
    }

    public NBTArray add(Boolean value)
    {
        data.add(value ? "1b" : "0b");
        return this;
    }

    public NBTArray add(NBTObject value)
    {
        data.add(value);
        return this;
    }
    
    public NBTArray addCustom(String value) {
        data.add(value);
        return this;
    }

    @Override
    public String toString()
    {
        String result = "[";
        result += Generator.join(",", data.toArray());
        result += "]";
        return result;
    }
}
