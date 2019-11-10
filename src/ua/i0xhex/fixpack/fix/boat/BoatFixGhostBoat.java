package ua.i0xhex.fixpack.fix.boat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import ua.i0xhex.fixpack.FixPack;
import ua.i0xhex.fixpack.Manager;
import ua.i0xhex.fixpack.config.Config;
import ua.i0xhex.fixpack.fix.BoatFixManager;

public class BoatFixGhostBoat implements Listener, Manager {
    private FixPack plugin;
    private BoatFixManager manager;
    
    private boolean enabled;
    
    private Set<UUID> passengerMarkSet = new HashSet<>();
    private Set<UUID> quitMarkSet = new HashSet<>();
    
    public BoatFixGhostBoat(FixPack plugin, BoatFixManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }
    
    @Override
    public void onEnable() {
        loadConfig();
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
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (player.getVehicle() != null)
            quitMarkSet.add(player.getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onVehicleDismount(VehicleExitEvent e) {
        if (enabled) {
            if (!(e.getVehicle() instanceof Boat)) return;
            
            LivingEntity exited = e.getExited();
            UUID exitedUUID = exited.getUniqueId();
        
            if (quitMarkSet.remove(exitedUUID)) return;
            if (passengerMarkSet.remove(exitedUUID)) return;
            if (!(exited instanceof Player)) return;
            
            Boat oldBoat = (Boat) e.getVehicle();
            World world = e.getVehicle().getWorld();
        
            Bukkit.getScheduler().runTask(plugin, () -> {
                List<Entity> passengers = oldBoat.getPassengers();
                passengers.forEach(p -> passengerMarkSet.add(p.getUniqueId()));
                oldBoat.eject();
                oldBoat.remove();

                Boat newBoat = (Boat) world.spawnEntity(oldBoat.getLocation(), oldBoat.getType());
                newBoat.setWoodType(oldBoat.getWoodType());
                passengers.forEach(newBoat::addPassenger);
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onVehicleDismount(VehicleDestroyEvent e) {
        if (!(e.getVehicle() instanceof Boat)) return;

        List<Entity> passengers = e.getVehicle().getPassengers();
        passengers.forEach(p -> passengerMarkSet.add(p.getUniqueId()));
    }

    private void loadConfig() {
        Config config = plugin.config();
        enabled = config.boatFixGhostBoat();
    }
}
