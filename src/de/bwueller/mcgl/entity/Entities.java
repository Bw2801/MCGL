package de.bwueller.mcgl.entity;

import de.bwueller.mcgl.Generator;
import de.bwueller.mcgl.constant.Gamemode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entities {

    private static enum SelectorType {
        SINGLE,
        MULTIPLE,
        RANDOM,
    }
    
    private static class EntityScore {
        public Integer min = null;
        public Integer max = null;
        
        public EntityScore(Integer min, Integer max) {
            this.min = min;
            this.max = max;
        }
    }
    
    public static Entities Player = Entities.getSingle(EntityType.Player);
    public static Entities AllPlayers = Entities.get(EntityType.Player);
    public static Entities ArmorStand = Entities.getSingle(EntityType.ArmorStand);
    
    private SelectorType selectorType = SelectorType.MULTIPLE;
    private EntityType entityType = EntityType.ANY;
    private String gamemode = Gamemode.ANY;

    Integer count = null;

    String name = null;
    String team = null;

    Integer minRadius = null,
            maxRadius = null;

    Integer x = null,
            y = null,
            z = null,
           dx = null,
           dy = null,
           dz = null;

    Integer rotationMinX = null,
            rotationMaxX = null,
            rotationMinY = null,
            rotationMaxY = null;

    Integer experienceMin = null;
    Integer experienceMax = null;

    Map<String, EntityScore> scores = new HashMap<>();

    String tag = null;

    public static Entities copy(Entities entities) {
        Entities copy = new Entities();
        copy(entities, copy);
        return copy;
    }

    public static void copy(Entities source, Entities target) {
        target.selectorType = source.selectorType;
        target.entityType = source.entityType;
        target.gamemode = source.gamemode;

        target.count = source.count;
        target.name = source.name;
        target.team = source.team;

        target.minRadius = source.minRadius;
        target.maxRadius = source.maxRadius;

        target.x = source.x;
        target.y = source.y;
        target.z = source.z;
        target.dx = source.dx;
        target.dy = source.dy;
        target.dz = source.dz;

        target.tag = source.tag;

        target.rotationMinX = source.rotationMinX;
        target.rotationMinY = source.rotationMinY;
        target.rotationMaxX = source.rotationMaxX;
        target.rotationMaxY = source.rotationMaxY;
        target.experienceMin = source.experienceMin;
        target.experienceMax = source.experienceMax;

        source.scores.keySet().stream().forEach((scoreName) -> {
            EntityScore sourceScore = source.scores.get(scoreName);
            target.scores.put(scoreName, new EntityScore(sourceScore.min, sourceScore.max));
        });
    }

    public static Entities get(EntityType type, Integer count) {
        Entities entities = new Entities();
        entities.selectorType = count != null && count == 1 ? SelectorType.SINGLE : SelectorType.MULTIPLE;
        entities.entityType = type;
        entities.count = count;
        return entities;
    }
    
    public static Entities get(EntityType type) {
        return get(type, null);
    }
    
    public static Entities get() {
        return get(EntityType.ANY, null);
    }

    public static Entities getSingle(EntityType type) {
        return get(type, 1);
    }
    
    public static Entities getSingle() {
        return getSingle(EntityType.ANY);
    }

    public static Entities getRandom(EntityType type, Integer count) {
        Entities entities = new Entities();
        entities.selectorType = SelectorType.RANDOM;
        entities.entityType = type;
        entities.count = count;
        return entities;
    }
    
    public static Entities getRandom(EntityType type) {
        return getRandom(type, null);
    }
    
    public static Entities getRandom() {
        return getRandom(EntityType.ANY, null);
    }

    public Entities withType(EntityType entityType) {
        Entities entities = copy(this);
        entities.entityType = entityType;
        return entities;
    }

    public Entities withName(String name) {
        Entities entities = copy(this);
        entities.name = name;
        return entities;
    }

    public Entities withTeam(String team) {
        Entities entities = copy(this);
        entities.team = team;
        return entities;
    }

    public Entities withScore(String objective, Integer min, Integer max) {
        Entities entities = copy(this);
        EntityScore score = new EntityScore(min, max);
        entities.scores.put(objective, score);
        return entities;
    }

    public Entities withMaxScore(String objective, int max) {
        return withScore(objective, null, max);
    }

    public Entities withMinScore(String objective, int min) {
        return withScore(objective, min, null);
    }

    public Entities clearScores() {
        Entities entities = copy(this);
        entities.scores.clear();
        return entities;
    }

    public Entities atCoordinates(int x, int y, int z, Integer maxRadius, Integer minRadius) {
        Entities entities = copy(this);
        entities.x = x;
        entities.y = y;
        entities.z = z;
        entities.dx = null;
        entities.dy = null;
        entities.dz = null;
        entities.minRadius = minRadius;
        entities.maxRadius = maxRadius;
        return entities;
    }
    
    public Entities atCoordinates(int x, int y, int z, Integer maxRadius) {
        return atCoordinates(x, y, z, maxRadius, null);
    }
    
    public Entities atCoordinates(int x, int y, int z) {
        return atCoordinates(x, y, z, 0, null);
    }

    public Entities InArea(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        Entities entities = copy(this);
        entities.x = x;
        entities.y = y;
        entities.z = z;
        entities.dx = xMax - xMin;
        entities.dy = yMax - yMin;
        entities.dz = zMax - zMin;
        entities.minRadius = null;
        entities.maxRadius = null;
        return entities;
    }

    public Entities InRadius(Integer min, Integer max) {
        Entities entities = copy(this);
        entities.minRadius = min;
        entities.maxRadius = max;
        return entities;
    }

    public Entities withRotation(Integer minXRotation, Integer maxXRotation, Integer minYRotation, Integer maxYRotation) {
        Entities entities = copy(this);
        entities.rotationMinX = minXRotation;
        entities.rotationMaxX = maxXRotation;
        entities.rotationMinY = minYRotation;
        entities.rotationMaxY = maxYRotation;
        return entities;
    }

    public Entities withHorizontalRotation(Integer min, Integer max) {
        return withRotation(min, max, null, null);
    }

    public Entities withVerticalRotation(Integer min, Integer max) {
        return withRotation(null, null, min, max);
    }

    public Entities withGamemode(String gamemode) {
        Entities entities = copy(this);
        entities.gamemode = gamemode;
        return entities;
    }

    public Entities withExperienceLevel(Integer min, Integer max) {
        Entities entities = copy(this);
        entities.experienceMin = min;
        entities.experienceMax = max;
        return entities;
    }

    public Entities withTag(String tag) {
        Entities entities = copy(this);
        entities.tag = tag;
        return entities;
    }
    
    public Entities withoutTag(String tag) {
        return withTag("!" + tag);
    }

    public String getSelectorString() {
        List<String> elements = new ArrayList<>();
        String result = "";

        if (selectorType == SelectorType.SINGLE) {
            if (entityType == EntityType.Player)
            {
                result = "@p";
            }
            else
            {
                result = "@e";
                elements.add("c=1");
            }
        }
        else if (selectorType == SelectorType.MULTIPLE) {
            if (entityType == EntityType.Player)
            {
                result = "@a";
            }
            else
            {
                result = "@e";
            }
            if (count != null) elements.add("c=" + count);
        }
        else if (selectorType == SelectorType.RANDOM) {
            result = "@r";
            if (count != null) elements.add("count=" + count);
        }

        if (name != null) elements.add("name=" + name);
        if (team != null) elements.add("team=" + team);
        if (entityType != EntityType.Player && entityType != EntityType.ANY) elements.add("type=" + entityType.toString());
        if (experienceMin != null) elements.add("lm=" + experienceMin);
        if (experienceMax != null) elements.add("l=" + experienceMax);
        if (!gamemode.equals(Gamemode.ANY)) elements.add("m=" + gamemode);

        if (x != null && y != null && z != null) {
            elements.add("x=" + x);
            elements.add("y=" + y);
            elements.add("z=" + z);
        }

        if (dx != null && dy != null && dz != null) {
            elements.add("dx=" + dx);
            elements.add("dy=" + dy);
            elements.add("dz=" + dz);
        }

        if (minRadius != null) elements.add("rm=" + minRadius);
        if (maxRadius != null) elements.add("r=" + maxRadius);
        if (rotationMinX != null) elements.add("rxm=" + rotationMinX);
        if (rotationMaxX != null) elements.add("rx=" + rotationMaxX);
        if (rotationMinY != null) elements.add("rym=" + rotationMinY);
        if (rotationMaxY != null) elements.add("ry=" + rotationMaxY);
        if (tag != null) elements.add("tag=" + tag);

        scores.keySet().stream().forEach((scoreName) -> {
            EntityScore score = scores.get(scoreName);
            if (score.min != null) elements.add("score_" + scoreName + "_min=" + score.min);
            if (score.max != null) {
                elements.add("score_" + scoreName + "=" + score.max);
            }
        });

        if (elements.size() > 0) {
            result += "[" + Generator.join(",", elements.toArray()) + "]";
        }

        return result;
    }
}
