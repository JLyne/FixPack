package ua.i0xhex.fixpack.fix.boat;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import ua.i0xhex.fixpack.FixPack;
import ua.i0xhex.fixpack.Manager;
import ua.i0xhex.fixpack.config.Config;
import ua.i0xhex.fixpack.fix.BoatFixManager;

public class BoatPreventSuffOnExit implements Listener, Manager {
    private FixPack plugin;
    private BoatFixManager manager;
    
    private boolean enabled;
    private Duration duration;
    
    private Cache<UUID, Byte> suffocationImmuneEntities;
    
    public BoatPreventSuffOnExit(FixPack plugin, BoatFixManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }
    
    @Override
    public void onEnable() {
        loadConfig();
        suffocationImmuneEntities = CacheBuilder.newBuilder()
                .expireAfterWrite(duration.toMillis(), TimeUnit.MILLISECONDS)
                .build();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void onDisable() {
        // ...
    }
    
    @Override
    public void onReload() {
        loadConfig();
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVehicleDismount(VehicleExitEvent e) {
        if (enabled) {
            if (!(e.getVehicle() instanceof Boat)) return;
            UUID uuid = e.getExited().getUniqueId();
            suffocationImmuneEntities.put(uuid, (byte) 0);
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBoatExitSuffocation(EntityDamageEvent e) {
        if (enabled) {
            if (e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) return;
            if (suffocationImmuneEntities.getIfPresent(e.getEntity().getUniqueId()) != null)
                e.setCancelled(true);
        }
    }
    
    private void loadConfig() {
        Config config = plugin.config();
        enabled = config.boatPreventSuffocationOnExit();
        duration = config.boatSuffocationImmuneDuration();
    }
}
