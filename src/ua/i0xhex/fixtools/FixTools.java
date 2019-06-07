package ua.i0xhex.fixtools;

import org.bukkit.plugin.java.JavaPlugin;

import ua.i0xhex.fixtools.config.Config;
import ua.i0xhex.fixtools.fix.BoatFix;

public class FixTools extends JavaPlugin implements Manager {
    
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
