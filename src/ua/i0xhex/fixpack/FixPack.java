package ua.i0xhex.fixpack;

import org.bukkit.plugin.java.JavaPlugin;

import ua.i0xhex.fixpack.config.Config;
import ua.i0xhex.fixpack.fix.BoatFix;

public class FixPack extends JavaPlugin implements Manager {
    
    private Config config;
    
    @Override
    public void onEnable() {
        config = new Config(this);
        loadFixes();
    }
    
    @Override
    public void onDisable() {
    
    }
    
    @Override
    public void onReload() {
        config = new Config(this);
    }
    
    // getters
    
    public Config config() {
        return config;
    }
    
    // internal
    
    private void loadFixes() {
        new BoatFix(this);
    }
}
