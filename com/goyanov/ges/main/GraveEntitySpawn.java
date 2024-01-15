package com.goyanov.ges.main;

import com.goyanov.ges.commands.CommandReload;
import com.goyanov.ges.events.CreatureSpawn;
import com.goyanov.ges.hooks.DummyProtocolLibHook;
import com.goyanov.ges.hooks.ProtocolLibHook;
import com.goyanov.ges.hooks.SimpleProtocolLibHook;
import com.goyanov.ges.utils.PluginSettings;
import com.goyanov.ges.utils.WrongLocationFixer;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GraveEntitySpawn extends JavaPlugin
{
    private static GraveEntitySpawn instance;

    private ProtocolLibHook protocolLibHook;

    public static GraveEntitySpawn getInstance()
    {
        return instance;
    }

    public boolean loadPlugin()
    {
        boolean withNoErrors = true;
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists())
            saveDefaultConfig();
        reloadConfig();
        PluginSettings.setPlaySound(getConfig().getBoolean("play-sound"));
        String soundName = getConfig().getString("spawn-sound.name");
        try
        {
            PluginSettings.setSpawnSoundName(Sound.valueOf(soundName));
        } catch (Exception e)
        {
            withNoErrors = false;
            getServer().getConsoleSender().sendMessage("§cConfig error! No such sound with name '" + soundName + "'!");
        }
        PluginSettings.setSpawnSoundLoudness((float) getConfig().getDouble("spawn-sound.loudness"));
        PluginSettings.setSpawnSoundPitch((float) getConfig().getDouble("spawn-sound.pitch"));
        PluginSettings.setSpawnSoundDelay(getConfig().getInt("spawn-sound.delay-in-ticks"));
        PluginSettings.setUseJockeyRider(getConfig().getBoolean("on-jockey-use-rider"));
        PluginSettings.setAllMobs(getConfig().getBoolean("all-mobs"));
        PluginSettings.setSpawnParticles(getConfig().getBoolean("spawn-particles"));
        PluginSettings.setSpawnDuration((float) (getConfig().getDouble("spawn-duration-seconds") * 20.0D));
        PluginSettings.setUseBlockUnderEntityForParticles(getConfig().getBoolean("use-block-under-entity-for-particles"));
        PluginSettings.setCanBeDamagedWhileSpawning(getConfig().getBoolean("can-be-damaged-while-spawning"));
        PluginSettings.getWhiteList().clear();
        for (String mobType : getConfig().getStringList("white-list-entities"))
        {
            try
            {
                PluginSettings.getWhiteList().add(EntityType.valueOf(mobType.toUpperCase()));
            } catch (Exception e)
            {
                withNoErrors = false;
                getServer().getConsoleSender().sendMessage("§cConfig error! No such entity type with name '" + mobType.toString() + "'!");
            }
        }
        PluginSettings.getBlackList().clear();
        for (String mobType : getConfig().getStringList("black-list-entities"))
        {
            try
            {
                PluginSettings.getBlackList().add(EntityType.valueOf(mobType.toUpperCase()));
            } catch (Exception e)
            {
                withNoErrors = false;
                getServer().getConsoleSender().sendMessage("§cConfig error! No such entity type with name '" + mobType.toString() + "'!");
            }
        }
        PluginSettings.getBannedWorlds().clear();
        for (String worldName : getConfig().getStringList("disallowed-worlds"))
            PluginSettings.getBannedWorlds().add(worldName);
        PluginSettings.getBlockedReasons().clear();
        for (String s : getConfig().getStringList("blocked-spawn-reasons"))
        {
            try
            {
                PluginSettings.getBlockedReasons().add(CreatureSpawnEvent.SpawnReason.valueOf(s.toUpperCase()));
            } catch (Exception e)
            {
                withNoErrors = false;
                getServer().getConsoleSender().sendMessage("§cConfig error! No such spawn reason with name '" + s + "'!");
            }
        }
        if (!PluginSettings.getUseBlockUnderEntityForParticles())
        {
            String name = getConfig().getString("spawn-particle-block");
            try
            {
                PluginSettings.setSpawnParticleBlockData(Bukkit.createBlockData(Material.valueOf(name)));
            } catch (Exception e)
            {
                PluginSettings.setUseBlockUnderEntityForParticles(true);
                withNoErrors = false;
                getServer().getConsoleSender().sendMessage("§cConfig error! No such material with name '" + name + "'! Try another one");
            }
        }
        return withNoErrors;
    }

    public void onEnable()
    {
        instance = this;
        loadPlugin();
        ((Logger) LogManager.getRootLogger()).addFilter((Filter) new WrongLocationFixer());
        getServer().getPluginManager().registerEvents((Listener) new CreatureSpawn(), (Plugin) this);
        getCommand("ges").setExecutor((CommandExecutor) new CommandReload());
        if (getServer().getPluginManager().getPlugin("ProtocolLib") != null)
        {
            this.protocolLibHook = (ProtocolLibHook) new SimpleProtocolLibHook(this);
        } else
        {
            this.protocolLibHook = (ProtocolLibHook) new DummyProtocolLibHook();
        }
    }

    public ProtocolLibHook getProtocolLibHook()
    {
        return this.protocolLibHook;
    }

    public void onDisable()
    {
        for (Entity ent : PluginSettings.getSpawningEntities())
            ent.remove();
    }
}
