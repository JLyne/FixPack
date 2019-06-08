package ua.i0xhex.fixpack.fix;

import java.util.ArrayList;
import java.util.List;

import ua.i0xhex.fixpack.FixPack;
import ua.i0xhex.fixpack.Manager;
import ua.i0xhex.fixpack.fix.boat.BoatFixGhostBoat;
import ua.i0xhex.fixpack.fix.boat.BoatPreventSuffOnExit;
import ua.i0xhex.fixpack.fix.boat.BoatSafeExitLocation;

public class BoatFixManager implements Manager {
    private FixPack plugin;
    
    private List<Manager> features;
    
    private BoatFixGhostBoat boatFixGhostBoat;
    private BoatPreventSuffOnExit boatPreventSuffOnExit;
    private BoatSafeExitLocation boatSafeExitLocation;
    
    public BoatFixManager(FixPack plugin) {
        this.plugin = plugin;
        
        features = new ArrayList<>();
        features.add(boatFixGhostBoat = new BoatFixGhostBoat(plugin, this));
        features.add(boatPreventSuffOnExit = new BoatPreventSuffOnExit(plugin, this));
        features.add(boatSafeExitLocation = new BoatSafeExitLocation(plugin, this));
    }
    
    // impl: Manager
    
    @Override
    public void onEnable() {
        features.forEach(Manager::onEnable);
    }
    
    @Override
    public void onDisable() {
        features.forEach(Manager::onDisable);
    }
    
    @Override
    public void onReload() {
        features.forEach(Manager::onReload);
    }
    
    // getters
    
    public BoatFixGhostBoat getBoatFixGhostBoat() {
        return boatFixGhostBoat;
    }
    
    public BoatPreventSuffOnExit getBoatPreventSuffOnExit() {
        return boatPreventSuffOnExit;
    }
    
    public BoatSafeExitLocation getBoatSafeExitLocation() {
        return boatSafeExitLocation;
    }
}
