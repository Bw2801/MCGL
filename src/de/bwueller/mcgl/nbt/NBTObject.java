package de.bwueller.mcgl.nbt;

import de.bwueller.mcgl.Generator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NBTObject {
    
    public static final NBTObject InGround = new NBTObject().set("inGround", true);

    public static NBTObject combine(NBTObject... objects) {
        NBTObject NBT = new NBTObject();
        for (NBTObject part : objects) {
            for (Iterator<String> it = part.data.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                NBT.setCustom(key, part.data.get(key));
            }
        }
        return NBT;
    }

    public final Map<String, Object> data = new HashMap<>();

    public NBTObject set(String key, String value)
    {
        data.put(key, "\"" + value + "\"");
        return this;
    }

    public NBTObject set(String key, String[] values)
    {
        data.put(key, "[\"" + Generator.join("\",\"", values) + "\"]");
        return this;
    }

    public NBTObject set(String key, Integer value)
    {
        data.put(key, value + "s");
        return this;
    }

    public NBTObject set(String key, Integer[] values)
    {
        data.put(key, "[" + Generator.join("s,", values) + "s]");
        return this;
    }

    public NBTObject set(String key, Double value)
    {
        data.put(key, value + "d");
        return this;
    }

    public NBTObject set(String key, Double[] values)
    {
        data.put(key, "[" + Generator.join("d,", values) + "d]");
        return this;
    }

    public NBTObject set(String key, Boolean value)
    {
        data.put(key, value ? "1b" : "0b");
        return this;
    }

    public NBTObject set(String key, NBTObject value)
    {
        data.put(key, value);
        return this;
    }

    public NBTObject set(String key, NBTArray value)
    {
        data.put(key, value);
        return this;
    }

    public NBTObject setCustom(String key, Object value)
    {
        data.put(key, value);
        return this;
    }

    @Override
    public String toString()
    {
        String result = "{";
        for (String part : data.keySet()) {
            result += part + ":" + data.get(part).toString() + ",";
        }
        if (data.size() > 0) result = result.substring(0, result.length() - 1);
        result += "}";

        return result;
    }
}
