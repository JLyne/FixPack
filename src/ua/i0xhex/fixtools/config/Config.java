package ua.i0xhex.fixtools.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import ua.i0xhex.fixtools.FixTools;
import ua.i0xhex.fixtools.config.yaml.bukkit.YamlConfiguration;

public class Config extends Configuration {
    
    private boolean boatFixGhostBoat;
    private boolean boatSafeExitLocation;
    private boolean boatPreventSuffocationOnExit;
    private Duration boatSuffocationImmuneDuration;
    
    public Config(FixTools plugin) {
        super(plugin, "config.yml", true);
        applyDefaults("config.yml");
        saveConfig();
        
        boatFixGhostBoat = config.getBoolean("boat.fix-ghost-boat");
        boatSafeExitLocation = config.getBoolean("boat.safe-exit-location");
        boatPreventSuffocationOnExit = config.getBoolean("boat.prevent-suffocation-on-exit");
        boatSuffocationImmuneDuration = Duration.ofSeconds(config.getLong("boat.suffocation-immune-seconds"));
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
}
