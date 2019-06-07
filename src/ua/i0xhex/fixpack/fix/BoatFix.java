package ua.i0xhex.fixpack.fix;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import ua.i0xhex.fixpack.FixPack;
import ua.i0xhex.fixpack.config.Config;

public class BoatFix implements Listener {
    
    private FixPack plugin;
    
    // config
    private boolean fixGhostBoat;
    private boolean safeExitLocation;
    private boolean preventSuffocationOnExit;
    
    // active
    private Set<UUID> passengersExitQuery = new HashSet<>();
    private Cache<UUID, Byte> suffocationImmuneEntities;
    
    public BoatFix(FixPack plugin) {
        this.plugin = plugin;
        
        Config config = plugin.config();
        
        fixGhostBoat = config.boatFixGhostBoat();
        safeExitLocation = config.boatSafeExitLocation();
        preventSuffocationOnExit = config.boatPreventSuffocationOnExit();
        Duration suffocationImmuneDuration = config.boatSuffocationImmuneDuration();
        
        if (preventSuffocationOnExit) {
            suffocationImmuneEntities = CacheBuilder.newBuilder()
                    .expireAfterWrite(suffocationImmuneDuration.toMillis(), TimeUnit.MILLISECONDS)
                    .build();
        }
    
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onVehicleDismount(VehicleExitEvent e) {
        if (!(e.getVehicle() instanceof Boat)) return;
        
        LivingEntity exited = e.getExited();
        UUID exitedUUID = exited.getUniqueId();
        
        // fix: suffocation on exit vehicle
        if (preventSuffocationOnExit)
            suffocationImmuneEntities.put(exitedUUID, (byte) 0);
        
        if (passengersExitQuery.remove(exitedUUID)) return;
        if (!(exited instanceof Player)) return;
        
        Boat oldBoat = (Boat) e.getVehicle();
        Player player = (Player) e.getExited();
        
        World world = e.getVehicle().getWorld();
        Location stopLocation = player.getLocation();
        
        Bukkit.getScheduler().runTask(plugin, () -> {
            // fix: ghost boat on exit
            if (fixGhostBoat) {
                List<Entity> passengers = oldBoat.getPassengers();
                passengers.forEach(p -> passengersExitQuery.add(p.getUniqueId()));
                oldBoat.eject();
                oldBoat.remove();
    
                Boat newBoat = (Boat) world.spawnEntity(oldBoat.getLocation(), oldBoat.getType());
                newBoat.setWoodType(oldBoat.getWoodType());
                passengers.forEach(newBoat::addPassenger);
            }
            
            // fix: unsafe exit location
            if (safeExitLocation) {
                player.teleport(stopLocation.add(0, 0.7, 0));
            }
        });
    }
    
    @EventHandler
    public void onBoatExitSuffocation(EntityDamageEvent e) {
        if (!preventSuffocationOnExit) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) return;
        
        UUID uuid = e.getEntity().getUniqueId();
        if (suffocationImmuneEntities.getIfPresent(uuid) != null) e.setCancelled(true);
    }
}
