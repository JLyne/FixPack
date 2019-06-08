package ua.i0xhex.fixpack;

import org.bukkit.plugin.java.JavaPlugin;

import ua.i0xhex.fixpack.config.Config;
import ua.i0xhex.fixpack.fix.BoatFix;
import ua.i0xhex.fixpack.fix.VillagerFix;

public class FixPack extends JavaPlugin implements Manager {
    
    private Config config;
    private String version;
    
    @Override
    public void onEnable() {
        config = new Config(this);
        version = getServer().getClass().getPackage().getName().split("\\.")[3];
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
    
    public String version() {
        return version;
    }
    
    // internal
    
    private void loadFixes() {
        new BoatFix(this);
        new VillagerFix(this);
    }
}
