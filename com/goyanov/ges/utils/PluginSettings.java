package com.goyanov.ges.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PluginSettings
{
    private static List<LivingEntity> spawningEntities = new ArrayList<>();

    private static boolean allMobs;

    private static boolean playSound;

    private static Sound spawnSoundName;

    private static float spawnSoundLoudness;

    private static float spawnSoundPitch;

    private static float spawnSoundDelay;

    private static boolean useJockeyRider;

    private static boolean spawnParticles;

    private static boolean useBlockUnderEntityForParticles;

    private static BlockData spawnParticleBlockData;

    private static boolean canBeDamagedWhileSpawning;

    private static float spawnDuration;

    private static List<EntityType> whiteList = new ArrayList<>();

    private static List<EntityType> blackList = new ArrayList<>();

    private static List<CreatureSpawnEvent.SpawnReason> blockedReasons = new ArrayList<>();

    private static List<String> bannedWorlds = new ArrayList<>();

    public static List<LivingEntity> getSpawningEntities()
    {
        return spawningEntities;
    }

    public static boolean getAllMobs()
    {
        return allMobs;
    }

    public static void setAllMobs(boolean allMobs)
    {
        PluginSettings.allMobs = allMobs;
    }

    public static boolean getPlaySound()
    {
        return playSound;
    }

    public static void setPlaySound(boolean playSound)
    {
        PluginSettings.playSound = playSound;
    }

    public static Sound getSpawnSoundName()
    {
        return spawnSoundName;
    }

    public static void setSpawnSoundName(Sound spawnSoundName)
    {
        PluginSettings.spawnSoundName = spawnSoundName;
    }

    public static float getSpawnSoundLoudness()
    {
        return spawnSoundLoudness;
    }

    public static void setSpawnSoundLoudness(float spawnSoundLoudness)
    {
        PluginSettings.spawnSoundLoudness = spawnSoundLoudness;
    }

    public static float getSpawnSoundPitch()
    {
        return spawnSoundPitch;
    }

    public static void setSpawnSoundPitch(float spawnSoundPitch)
    {
        PluginSettings.spawnSoundPitch = spawnSoundPitch;
    }

    public static float getSpawnSoundDelay()
    {
        return spawnSoundDelay;
    }

    public static void setSpawnSoundDelay(float spawnSoundDelay)
    {
        PluginSettings.spawnSoundDelay = spawnSoundDelay;
    }

    public static boolean getSpawnParticles()
    {
        return spawnParticles;
    }

    public static void setUseJockeyRider(boolean useRider)
    {
        useJockeyRider = useRider;
    }

    public static boolean getUseJockeyRider()
    {
        return useJockeyRider;
    }

    public static void setSpawnParticles(boolean spawnParticles)
    {
        PluginSettings.spawnParticles = spawnParticles;
    }

    public static boolean getUseBlockUnderEntityForParticles()
    {
        return useBlockUnderEntityForParticles;
    }

    public static void setUseBlockUnderEntityForParticles(boolean useBlockUnderEntityForParticles)
    {
        PluginSettings.useBlockUnderEntityForParticles = useBlockUnderEntityForParticles;
    }

    public static boolean getCanBeDamagedWhileSpawning()
    {
        return canBeDamagedWhileSpawning;
    }

    public static void setCanBeDamagedWhileSpawning(boolean canBeDamagedWhileSpawning)
    {
        PluginSettings.canBeDamagedWhileSpawning = canBeDamagedWhileSpawning;
    }

    public static BlockData getSpawnParticleBlockData()
    {
        return spawnParticleBlockData;
    }

    public static void setSpawnParticleBlockData(BlockData spawnParticleBlockData)
    {
        PluginSettings.spawnParticleBlockData = spawnParticleBlockData;
    }

    public static float getSpawnDuration()
    {
        return spawnDuration;
    }

    public static void setSpawnDuration(float spawnDuration)
    {
        PluginSettings.spawnDuration = spawnDuration;
    }

    public static List<EntityType> getWhiteList()
    {
        return whiteList;
    }

    public static List<EntityType> getBlackList()
    {
        return blackList;
    }

    public static List<CreatureSpawnEvent.SpawnReason> getBlockedReasons()
    {
        return blockedReasons;
    }

    public static List<String> getBannedWorlds()
    {
        return bannedWorlds;
    }
}
