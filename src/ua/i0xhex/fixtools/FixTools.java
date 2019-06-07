package ua.i0xhex.fixtools;

import org.bukkit.plugin.java.JavaPlugin;

import ua.i0xhex.fixtools.config.Config;

public class FixTools extends JavaPlugin implements Manager {
    
    private Config config;
    
    @Override
    public void onEnable() {
        config = new Config(this);
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
}
