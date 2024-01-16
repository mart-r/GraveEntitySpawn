package ges.events;

import ges.main.GraveEntitySpawn;
import ges.utils.PluginSettings;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

public class CreatureSpawn implements Listener
{
    private final Set<Entity> passengers = new HashSet<>();

    private final Set<Entity> noSpawnPassengers = new HashSet<>();

    private void createParticles(Location loc, BlockData data)
    {
        loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 10, data);
    }

    private void fixChunkBoundary(LivingEntity ent, Location entLoc)
    {
        BoundingBox box = ent.getBoundingBox();
        double minX = box.getMinX();
        double minZ = box.getMinZ();
        double maxX = box.getMaxX();
        double maxZ = box.getMaxZ();
        World world = entLoc.getWorld();
        double y = entLoc.getY();
        Location c1 = new Location(world, minX, y, minZ);
        Location c2 = new Location(world, minX, y, maxZ);
        Location c3 = new Location(world, maxX, y, minZ);
        boolean sameX = (c1.getChunk() == c3.getChunk());
        boolean sameZ = (c1.getChunk() == c2.getChunk());
        if (sameX && sameZ)
            return;
        if (!sameX)
        {
            double move = box.getWidthX() / 2.0D;
            if (c1.getChunk() == entLoc.getChunk())
            {
                entLoc.add(-move, 0.0D, 0.0D);
            } else
            {
                entLoc.add(move, 0.0D, 0.0D);
            }
        }
        if (!sameZ)
        {
            double move = box.getWidthZ() / 2.0D;
            if (c1.getChunk() == entLoc.getChunk())
            {
                entLoc.add(0.0D, 0.0D, -move);
            } else
            {
                entLoc.add(0.0D, 0.0D, move);
            }
        }
    }

    private void smoothEntitySpawnFromGrave(final LivingEntity ent)
    {
        if (this.passengers.contains(ent))
            return;
        if (this.noSpawnPassengers.contains(ent))
        {
            this.noSpawnPassengers.remove(ent);
            return;
        }
        final Location particleLocation = ent.getLocation();
        final Location entLoc = particleLocation.clone();
        fixChunkBoundary(ent, entLoc);
        final Entity passenger = ent.getPassenger();
        if (passenger != null)
            this.passengers.add(passenger);
        if (!entLoc.clone().add(0.0D, -1.0D, 0.0D).getBlock().getType().isSolid() || entLoc.getBlock().getType().toString().contains("WATER"))
            return;
        PluginSettings.getSpawningEntities().add(ent);
        entLoc.add(0.0D, -2.0D, 0.0D);
        ent.teleport(entLoc);
        if (passenger != null)
        {
            GraveEntitySpawn.getInstance().getProtocolLibHook().addEntityToChange(ent);
            if (passenger instanceof LivingEntity)
                GraveEntitySpawn.getInstance().getProtocolLibHook().addEntityToChange((LivingEntity) passenger);
        }
        Block blockUnderEntity = particleLocation.clone().add(0.0D, -1.0D, 0.0D).getBlock();
        final BlockData blockData = PluginSettings.getUseBlockUnderEntityForParticles() ? blockUnderEntity.getBlockData() : PluginSettings.getSpawnParticleBlockData();
        final float step = 1.0F / PluginSettings.getSpawnDuration() * 2.0F;
        (new BukkitRunnable()
        {
            int soundLoopCounter = 0;

            public void run()
            {
                if (!CreatureSpawn.this.isValid(ent, entLoc))
                {
                    PluginSettings.getSpawningEntities().remove(ent);
                    ent.remove();
                    if (passenger != null)
                        CreatureSpawn.this.passengers.remove(passenger);
                    cancel();
                    return;
                }
                if (entLoc.getBlock().getType().isSolid() || entLoc.clone().add(0.0D, 1.0D, 0.0D).getBlock().getType().isSolid())
                {
                    if (PluginSettings.getSpawnParticles())
                        CreatureSpawn.this.createParticles(particleLocation, blockData);
                    entLoc.add(0.0D, step, 0.0D);
                } else
                {
                    if (passenger != null && !passenger.equals(ent.getPassenger()))
                        ent.addPassenger(passenger);
                    PluginSettings.getSpawningEntities().remove(ent);
                    if (passenger != null)
                        CreatureSpawn.this.passengers.remove(passenger);
                    cancel();
                    return;
                }
                ent.teleport(entLoc);
                if (passenger != null)
                    passenger.teleport(entLoc);
                if (PluginSettings.getPlaySound() && this.soundLoopCounter % PluginSettings.getSpawnSoundDelay() == 0.0F)
                {
                    entLoc.getWorld().playSound(particleLocation, PluginSettings.getSpawnSoundName(), PluginSettings.getSpawnSoundLoudness(), PluginSettings.getSpawnSoundPitch());
                    this.soundLoopCounter = 0;
                }
                this.soundLoopCounter++;
            }
        }).runTaskTimer((Plugin) GraveEntitySpawn.getInstance(), 1L, 1L);
    }

    private boolean isValid(LivingEntity ent, Location entLoc)
    {
        if (ent.isDead() || !ent.isValid())
            return false;
        if (!entLoc.getWorld().isChunkLoaded(entLoc.getBlockX() >> 4, entLoc.getBlockZ() >> 4))
            return false;
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(CreatureSpawnEvent e)
    {
        if (!e.isCancelled())
        {
            LivingEntity spawnedEntity = e.getEntity();
            EntityType checkType = getCheckType(spawnedEntity);
            if (isApplicable(e.getSpawnReason(), spawnedEntity.getLocation(), checkType))
            {
                smoothEntitySpawnFromGrave(spawnedEntity);
            } else
            {
                Entity passenger = spawnedEntity.getPassenger();
                if (passenger != null && isApplicable(passenger.getType()))
                    this.noSpawnPassengers.add(passenger);
            }
        }
    }

    private EntityType getCheckType(LivingEntity entity)
    {
        if (!PluginSettings.getUseJockeyRider())
            return entity.getType();
        Entity passenger = entity.getPassenger();
        if (passenger == null)
            return entity.getType();
        return passenger.getType();
    }

    private boolean isApplicable(CreatureSpawnEvent.SpawnReason reason, Location loc, EntityType type)
    {
        if (PluginSettings.getBlockedReasons().contains(reason))
            return false;
        if (!loc.getChunk().isLoaded())
            return false;
        if (PluginSettings.getBannedWorlds().contains(loc.getWorld().getName()))
            return false;
        return isApplicable(type);
    }

    private boolean isApplicable(EntityType type)
    {
        if (PluginSettings.getBlackList().contains(type))
            return false;
        return (PluginSettings.getAllMobs() || PluginSettings.getWhiteList().contains(type));
    }

    @EventHandler
    public void noDamage(EntityDamageEvent e)
    {
        Entity ent = e.getEntity();
        if (PluginSettings.getSpawningEntities().contains(ent) || this.passengers.contains(ent))
        {
            if (!PluginSettings.getCanBeDamagedWhileSpawning())
            {
                e.setCancelled(true);
                return;
            }
            if (e.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION) || e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            {
                e.setCancelled(true);
                return;
            }
        }
    }
}
