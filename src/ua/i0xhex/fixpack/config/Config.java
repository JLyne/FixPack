package ua.i0xhex.fixpack.config;

import java.time.Duration;

import ua.i0xhex.fixpack.FixPack;

public class Config extends Configuration {
    
    private boolean boatFixGhostBoat;
    private boolean boatSafeExitLocation;
    private boolean boatPreventSuffocationOnExit;
    private Duration boatSuffocationImmuneDuration;
    
    private boolean villagerLockRandomTrades;
    
    public Config(FixPack plugin) {
        super(plugin, "config.yml", true);
        applyDefaults("config.yml");
        saveConfig();
        
        boatFixGhostBoat = config.getBoolean("boat.fix-ghost-boat");
        boatSafeExitLocation = config.getBoolean("boat.safe-exit-location");
        boatPreventSuffocationOnExit = config.getBoolean("boat.prevent-suffocation-on-exit");
        boatSuffocationImmuneDuration = Duration.ofSeconds(config.getLong("boat.suffocation-immune-seconds"));
        
        villagerLockRandomTrades = config.getBoolean("villager.lock-random-trades");
    }
    
    // getters
    
    public boolean boatFixGhostBoat() {
        return boatFixGhostBoat;
    }
    
    public boolean boatSafeExitLocation() {
        return boatSafeExitLocation;
    }
    
    public boolean boatPreventSuffocationOnExit() {
        return boatPreventSuffocationOnExit;
    }
    
    public Duration boatSuffocationImmuneDuration() {
        return boatSuffocationImmuneDuration;
    }
    
    public boolean villagerLockRandomTrades() {
        return villagerLockRandomTrades;
    }
}
