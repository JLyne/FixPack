package ua.i0xhex.fixpack;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import ua.i0xhex.fixpack.config.Config;
import ua.i0xhex.fixpack.config.Lang;
import ua.i0xhex.fixpack.fix.BoatFixManager;
import ua.i0xhex.fixpack.fix.VillagerFixManager;

public class FixPack extends JavaPlugin implements Manager {
    
    private Lang lang;
    private Config config;
    private String version;
    
    private List<Manager> fixManagers;
    
    private BoatFixManager boatFixManager;
    private VillagerFixManager villagerFixManager;
    
    @Override
    public void onEnable() {
        lang = new Lang(this);
        config = new Config(this);
        version = getServer().getClass().getPackage().getName().split("\\.")[3];
        loadFixManagers();
    }
    
    @Override
    public void onDisable() {
        fixManagers.forEach(Manager::onDisable);
    }
    
    @Override
    public void onReload() {
        config = new Config(this);
        fixManagers.forEach(Manager::onReload);
    }
    
    // getters
    
    public Lang lang() {
        return lang;
    }
    
    public Config config() {
        return config;
    }
    
    public String version() {
        return version;
    }
    
    public BoatFixManager getBoatFixManager() {
        return boatFixManager;
    }
    
    public VillagerFixManager getVillagerFixManager() {
        return villagerFixManager;
    }
    
    // internal
    
    private void loadFixManagers() {
        fixManagers = new ArrayList<>();
        fixManagers.add(boatFixManager = new BoatFixManager(this));
        fixManagers.add(villagerFixManager = new VillagerFixManager(this));
        fixManagers.forEach(Manager::onEnable);
    }
}
