package de.bwueller.mcgl.command;

import de.bwueller.mcgl.coordinate.AbsoluteCoordinates;
import de.bwueller.mcgl.coordinate.Area;
import de.bwueller.mcgl.coordinate.Coordinates;
import de.bwueller.mcgl.coordinate.Rotation;
import de.bwueller.mcgl.entity.Entities;
import de.bwueller.mcgl.entity.EntityType;
import de.bwueller.mcgl.nbt.NBTObject;
import de.bwueller.mcgl.nbt.text.NBTText;

/**
 * Implements overload methods
 */
public abstract class CommandHandler implements ImplementedCommands {

    public abstract void customCommand(String command, boolean ifPrevious);
    
    public void clearItemFromInventory(Entities players, String item, int dataValue, int damount, NBTObject NBT) { clearItemFromInventory(players, item, dataValue, damount, NBT, false); }
    public void clearItemFromInventory(Entities players, String item, int dataValue, int damount) { clearItemFromInventory(players, item, dataValue, damount, null); }
    public void clearItemFromInventory(Entities players, String item, int dataValue) { clearItemFromInventory(players, item, dataValue, -1); }
    public void clearItemFromInventory(Entities players, String item) { clearItemFromInventory(players, item, -1); }
    
    public void cloneArea(Area source, Coordinates target, String mask, String mode, String block) { cloneArea(source, target, mask, mode, block, false); }
    public void cloneArea(Area source, Coordinates target, String mask, String mode) { cloneArea(source, target, mask, mode, null); }
    public void cloneArea(Area source, Coordinates target, String mask) { cloneArea(source, target, mask, "normal"); }
    public void cloneArea(Area source, Coordinates target) { cloneArea(source, target, "replace"); }
    
    public void giveEffect(Entities entities, String effect, int duration, int aplifier, boolean hideParticles) { giveEffect(entities, effect, duration, aplifier, hideParticles, false); }
    public void giveEffect(Entities entities, String effect, int duration, int aplifier) { giveEffect(entities, effect, duration, aplifier, false); }
    public void giveEffect(Entities entities, String effect, int duration) { giveEffect(entities, effect, duration, 0); }
    public void giveEffect(Entities entities, String effect) { giveEffect(entities, effect, 30); }
    
    public void fillArea(Area area, String block, int dataValue, String mode, NBTObject NBT) { fillArea(area, block, dataValue, mode, NBT, false); }
    public void fillArea(Area area, String block, int dataValue, String mode) { fillArea(area, block, dataValue, mode, null); }
    public void fillArea(Area area, String block, int dataValue) { fillArea(area, block, dataValue, "keep"); }
    public void fillArea(Area area, String block) { fillArea(area, block, 0); }
    
    public void replaceArea(Area area, String block, int dataValue, String replaceWith, int replaceWithDataValue) { replaceArea(area, block, dataValue, replaceWith, replaceWithDataValue, false); }
    public void replaceArea(Area area, String block, int dataValue, String replaceWith) { replaceArea(area, block, dataValue, replaceWith, -1); }
    public void replaceArea(Area area, String block, int dataValue) { replaceArea(area, block, dataValue, null); }
    public void replaceArea(Area area, String block) { replaceArea(area, block, 0); }
    
    public void giveItem(Entities players, String item, int amount, int dataValue, NBTObject NBT) { giveItem(players, item, amount, dataValue, NBT, false); }
    public void giveItem(Entities players, String item, int amount, int dataValue) { giveItem(players, item, amount, dataValue, null); }
    public void giveItem(Entities players, String item, int amount) { giveItem(players, item, amount, 0); }
    public void giveItem(Entities players, String item) { giveItem(players, item, 1); }
    
    public void kill(Entities entities, NBTObject NBT) { kill(entities, NBT, false); }
    public void kill(Entities entities) { kill(entities, null); }
    
    public void showParticles(String particle, Coordinates location, AbsoluteCoordinates distance, double speed, int count, String mode, Entities players, String parameters) { showParticles(particle, location, distance, speed, count, mode, players, parameters, false); }
    public void showParticles(String particle, Coordinates location, AbsoluteCoordinates distance, double speed, int count, String mode, Entities players) { showParticles(particle, location, distance, speed, count, mode, players, ""); }
    public void showParticles(String particle, Coordinates location, AbsoluteCoordinates distance, double speed, int count, String mode) { showParticles(particle, location, distance, speed, count, mode, null); }
    public void showParticles(String particle, Coordinates location, AbsoluteCoordinates distance, double speed, int count) { showParticles(particle, location, distance, speed, count, "default"); }
    public void showParticles(String particle, Coordinates location, AbsoluteCoordinates distance, double speed) { showParticles(particle, location, distance, speed, 0); }
    
    public void playSound(String sound, String source, Entities players, Coordinates location, double volume, double pitch, double minimumVolume) { playSound(sound, source, players, location, volume, pitch, minimumVolume, false); }
    public void playSound(String sound, String source, Entities players, Coordinates location, double volume, double pitch) { playSound(sound, source, players, location, volume, pitch, 0.0); }
    public void playSound(String sound, String source, Entities players, Coordinates location, double volume) { playSound(sound, source, players, location, volume, 1.0); }
    public void playSound(String sound, String source, Entities players, Coordinates location) { playSound(sound, source, players, location, 1.0); }
    
    public void replaceBlockItem(Coordinates location, String slot, String item, int amount, int dataValue, NBTObject NBT) { replaceBlockItem(location, slot, item, amount, dataValue, NBT, false); }
    public void replaceBlockItem(Coordinates location, String slot, String item, int amount, int dataValue) { replaceBlockItem(location, slot, item, amount, dataValue, null); }
    public void replaceBlockItem(Coordinates location, String slot, String item, int amount) { replaceBlockItem(location, slot, item, amount, 0); }
    public void replaceBlockItem(Coordinates location, String slot, String item) { replaceBlockItem(location, slot, item, 1); }
    
    public void replaceEntityItem(Entities entities, String slot, String item, int amount, int dataValue, NBTObject NBT) { replaceEntityItem(entities, slot, item, amount, dataValue, NBT, false); }
    public void replaceEntityItem(Entities entities, String slot, String item, int amount, int dataValue) { replaceEntityItem(entities, slot, item, amount, dataValue, null); }
    public void replaceEntityItem(Entities entities, String slot, String item, int amount) { replaceEntityItem(entities, slot, item, amount, 0); }
    public void replaceEntityItem(Entities entities, String slot, String item) { replaceEntityItem(entities, slot, item, 1); }
    
    public void setBlock(Coordinates location, String block, int dataValue, String mode, NBTObject NBT) { setBlock(location, block, dataValue, mode, NBT, false); }
    public void setBlock(Coordinates location, String block, int dataValue, String mode) { setBlock(location, block, dataValue, mode, null); }
    public void setBlock(Coordinates location, String block, int dataValue) { setBlock(location, block, dataValue, "replace"); }
    public void setBlock(Coordinates location, String block) { setBlock(location, block, 0); }
    
    public void setSpawnPoint(Entities players, Coordinates location) { setSpawnPoint(players, location, false); }
    public void setSpawnPoint(Entities players) { setSpawnPoint(players, null); }
    
    public void summonEntity(EntityType type, Coordinates location, NBTObject NBT) { summonEntity(type, location, NBT, false); }
    public void summonEntity(EntityType type, Coordinates location) { summonEntity(type, location, null); }
    
    public void testForEntity(Entities entities, NBTObject NBT) { testForEntity(entities, NBT, false); }
    public void testForEntity(Entities entities) { testForEntity(entities, null); }
    
    public void testForBlock(Coordinates location, String block, int dataValue, NBTObject NBT) { testForBlock(location, block, dataValue, NBT, false); }
    public void testForBlock(Coordinates location, String block, int dataValue) { testForBlock(location, block, dataValue, null); }
    public void testForBlock(Coordinates location, String block) { testForBlock(location, block, -1); }
    
    public void testForBlocks(Area source, Coordinates target, String mode) { testForBlocks(source, target, mode, false); }
    public void testForBlocks(Area source, Coordinates target) { testForBlocks(source, target, "all"); }
    
    public void teleportToLocation(Entities entities, Coordinates target, Rotation rotation) { teleportToLocation(entities, target, rotation, false); }
    public void teleportToLocation(Entities entities, Coordinates target) { teleportToLocation(entities, target, null); }
    
    public void setWeather(String weather, int duration) { setWeather(weather, duration, false); }
    public void setWeather(String weather) { setWeather(weather, 9000); }
    
    public void increaseWorldBorder(int distance, int time) { increaseWorldBorder(distance, time, false); }
    public void increaseWorldBorder(int distance) { increaseWorldBorder(distance, 0); }
    
    public void setWorldBorder(int distance, int time) { setWorldBorder(distance, time, false); }
    public void setWorldBorder(int distance) { setWorldBorder(distance, 0); }
    
    public void addScoreboardObjective(String objective, String criteria, String displayName) { addScoreboardObjective(objective, criteria, displayName, false); }
    public void addScoreboardObjective(String objective, String criteria) { addScoreboardObjective(objective, criteria, ""); }
    
    public void setScore(Entities entities, String objective, int score, NBTObject NBT) { setScore(entities, objective, score, NBT, false); }
    public void setScore(Entities entities, String objective, int score) { setScore(entities, objective, score, null); }
    
    public void addToScore(Entities entities, String objective, int amount, NBTObject NBT) { addToScore(entities, objective, amount, NBT, false); }
    public void addToScore(Entities entities, String objective, int amount) { addToScore(entities, objective, amount, null); }
    
    public void removeFromScore(Entities entities, String objective, int amount, NBTObject NBT) { removeFromScore(entities, objective, amount, NBT, false); }
    public void removeFromScore(Entities entities, String objective, int amount) { removeFromScore(entities, objective, amount, null); }
    
    public void testForScore(Entities entities, String objective, int min, int max) { testForScore(entities, objective, min, max, false); }
    public void testForScore(Entities entities, String objective, int min) { testForScore(entities, objective, min, 2147483647); }
    
    public void modifyScore(Entities target, String targetObjective, String operation, Entities sourceEntities, String sourceObjective) { modifyScore(target, targetObjective, operation, sourceEntities, sourceObjective, false); }
    public void modifyScore(Entities target, String targetObjective, String operation, Entities sourceEntities) { modifyScore(target, targetObjective, operation, sourceEntities, null); }
    
    public void addTeam(String name, String displayName) { addTeam(name, displayName, false); }
    public void addTeam(String name) { addTeam(name, ""); }
    
    public void addTag(Entities entities, String tag, NBTObject NBT) { addTag(entities, tag, NBT, false); }
    public void addTag(Entities entities, String tag) { addTag(entities, tag, null); }
    
    public void removeTag(Entities entities, String tag, NBTObject NBT) { removeTag(entities, tag, NBT, false); }
    public void removeTag(Entities entities, String tag) { removeTag(entities, tag, null); }
    
    public void setWorldBorderCenter(Coordinates center) { setWorldBorderCenter(center, false); }
    public void setWorldBorderDamageRate(int damagePerBlock) { setWorldBorderDamageRate(damagePerBlock, false); }
    public void setWorldBorderDamageBuffer(int distance) { setWorldBorderDamageBuffer(distance, false); }
    public void setWorldBorderWarningDistance(int distance) { setWorldBorderWarningDistance(distance, false); }
    public void setWorldBorderWarningTime(int time) { setWorldBorderWarningTime(time, false); }
    public void addExperience(Entities players, int amount) { addExperience(players, amount, false); }
    public void addExperienceLevels(Entities players, int amount) { addExperienceLevels(players, amount, false); }
    public void removeExperience(Entities players) { removeExperience(players, false); }
    public void teleportToEntity(Entities entities, Entities target) { teleportToEntity(entities, target, false); }
    public void addToTrigger(String objective, int value) { addToTrigger(objective, value, false); }
    public void setTrigger(String objective, int value) { setTrigger(objective, value, false); }
    public void clearEntityStat(Entities entities, String stat) { clearEntityStat(entities, stat, false); }
    public void setEntityStat(Entities entities, String stat, Entities targets, String objective) { setEntityStat(entities, stat, targets, objective, false); }
    public void clearBlockStat(Coordinates location, String stat) { clearBlockStat(location, stat, false); }
    public void setBlockStat(Coordinates location, String stat, Entities targets, String objective) { setBlockStat(location, stat, targets, objective, false); }
    public void spreadEntities(Entities entities, Coordinates center, double targetDistance, double range, boolean respectTeams) { spreadEntities(entities, center, targetDistance, range, respectTeams, false); }
    public void setGlobalSpawn(Coordinates location) { setGlobalSpawn(location, false); }
    public void setTime(int value) { setTime(value, false); }
    public void addTime(int value) { addTime(value, false); }
    public void queryTime(String value) { queryTime(value, false); }
    public void toggleDownfall() { toggleDownfall(false); }
    public void sendChatMessage(String message) { sendChatMessage(message, false); }
    public void sendPrivateMessage(Entities players, String message) { sendPrivateMessage(players, message, false); }
    public void sendRawChatMessage(Entities players, NBTText message) { sendRawChatMessage(players, message, false); }
    public void sendStatusMessage(String message) { sendStatusMessage(message, false); }
    public void setGamemode(Entities players, String gamemode) { setGamemode(players, gamemode, false); }
    public void setGamerule(String gamerule, Object value) { setGamerule(gamerule, value, false); }
    public void giveAchievement(Entities players, String achievement) { giveAchievement(players, achievement, false); }
    public void takeAchievement(Entities players, String achievement) { takeAchievement(players, achievement, false); }
    public void clearInventory(Entities players) { clearInventory(players, false); }
    public void setDefaultGamemode(String gamemode) { setDefaultGamemode(gamemode, false); }
    public void setDifficulty(String difficulty) { setDifficulty(difficulty, false); }
    public void takeEffect(Entities entities, String effect) { takeEffect(entities, effect, false); }
    public void clearEffects(Entities entities) { clearEffects(entities, false); }
    public void enchant(Entities players, String enchantment, int level) { enchant(players, enchantment, level, false); }
    public void enchant(Entities players, String enchantment) { enchant(players, enchantment, 1); }
    public void setBlockData(Coordinates coordinates, NBTObject NBT) { setBlockData(coordinates, NBT, false); }
    public void setEntityData(Entities entities, NBTObject NBT) { setEntityData(entities, NBT, false); }
    public void clearTitle(Entities players) { clearTitle(players, false); }
    public void resetTitle(Entities players) { resetTitle(players, false); }
    public void setTitleTimes(Entities players, int fadeIn, int show, int fadeOut) { setTitleTimes(players, fadeIn, show, fadeOut, false); }
    public void setSubtitle(Entities players, NBTText title) { setSubtitle(players, title, false); }
    public void showTitle(Entities players, NBTText title) { showTitle(players, title, false); }
    public void removeScoreboardObjective(String objective) { removeScoreboardObjective(objective, false); }
    public void showScoreboardObjective(String objective, String slot) { showScoreboardObjective(objective, slot, false); }
    public void hideScoreboard(String slot) { hideScoreboard(slot, false); }
    public void resetScore(Entities entities, String objective) { resetScore(entities, objective, false); }
    public void enableTrigger(Entities players, String trigger) { enableTrigger(players, trigger, false); }
    public void removeTeam(String team) { removeTeam(team, false); }
    public void emptyTeam(String team) { emptyTeam(team, false); }
    public void addEntityToTeam(Entities entities, String team) { addEntityToTeam(entities, team, false); }
    public void removeEntityFromTeam(Entities entities, String team) { removeEntityFromTeam(entities, team, false); }
    public void removeEntityFromAllTeams(Entities entities) { removeEntityFromAllTeams(entities, false); }
    public void setTeamOption(String team, String option, String value) { setTeamOption(team, option, value, false); }
}
