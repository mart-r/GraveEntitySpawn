package ges.hooks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.reflect.StructureModifier;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleProtocolLibHook extends PacketAdapter implements ProtocolLibHook
{
    private static final double CHANGE_Y = -2.0D;

    private final JavaPlugin plugin;

    private final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private final Set<LivingEntity> toCheck = new HashSet<>();

    public SimpleProtocolLibHook(JavaPlugin plugin)
    {
        super((Plugin) plugin, ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Server.SPAWN_ENTITY_LIVING});
        this.plugin = plugin;
        this.protocolManager.addPacketListener((PacketListener) this);
    }

    public void addEntityToChange(LivingEntity entity)
    {
        this.toCheck.add(entity);
    }

    private void removeEntity(LivingEntity ent)
    {
        this.toCheck.remove(ent);
    }

    public void onPacketSending(PacketEvent event)
    {
        if (event.getPacketType() != PacketType.Play.Server.SPAWN_ENTITY_LIVING)
            return;
        PacketContainer packet = event.getPacket();
        LivingEntity ent = identifyEntity(packet);
        if (ent == null)
            return;
        try
        {
            changeY(packet);
        } catch (Exception e)
        {
            this.plugin.getLogger().warning("Unable to change Y of spawning entity " + ent + "(" + ent.getEntityId() + "):");
            e.printStackTrace();
        }
        removeEntity(ent);
    }

    private LivingEntity identifyEntity(PacketContainer packet)
    {
        StructureModifier<Integer> ints = packet.getIntegers();
        int entityId = ((Integer) ints.read(0)).intValue();
        for (LivingEntity ent : this.toCheck)
        {
            int curId = ent.getEntityId();
            if (entityId == curId)
                return ent;
        }
        return null;
    }

    private void changeY(PacketContainer packet)
    {
        StructureModifier<Double> doubles = packet.getDoubles();
        double prev = ((Double) doubles.read(1)).doubleValue();
        doubles.write(1, Double.valueOf(prev + -2.0D));
    }
}
