package ua.i0xhex.fixpack.fix.boat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

import ua.i0xhex.fixpack.FixPack;
import ua.i0xhex.fixpack.Manager;
import ua.i0xhex.fixpack.config.Config;
import ua.i0xhex.fixpack.fix.BoatFixManager;

public class BoatSafeExitLocation implements Listener, Manager {
    private FixPack plugin;
    private BoatFixManager manager;
    
    private boolean enabled;
    
    public BoatSafeExitLocation(FixPack plugin, BoatFixManager manager) {
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
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVehicleDismount(VehicleExitEvent e) {
        if (enabled) {
            if (!(e.getVehicle() instanceof Boat)) return;
    
            LivingEntity exited = e.getExited();
            Location stopLocation = exited.getLocation();
    
            Bukkit.getScheduler().runTask(plugin, () ->
                exited.teleport(stopLocation.add(0, 0.7, 0)));
        }
    }
    
    private void loadConfig() {
        Config config = plugin.config();
        enabled = config.boatSafeExitLocation();
    }
}
