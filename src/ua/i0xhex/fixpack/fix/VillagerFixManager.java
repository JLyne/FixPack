package ua.i0xhex.fixpack.fix;

import java.util.ArrayList;
import java.util.List;

import ua.i0xhex.fixpack.FixPack;
import ua.i0xhex.fixpack.Manager;
import ua.i0xhex.fixpack.fix.villager.VillagerLockRandomTrades;

public class VillagerFixManager implements Manager {
    private FixPack plugin;
    
    private List<Manager> features;
    
    private VillagerLockRandomTrades villagerLockRandomTrades;
    
    public VillagerFixManager(FixPack plugin) {
        this.plugin = plugin;
    
        features = new ArrayList<>();
        features.add(villagerLockRandomTrades = new VillagerLockRandomTrades(plugin, this));
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
    
    public VillagerLockRandomTrades getVillagerLockRandomTrades() {
        return villagerLockRandomTrades;
    }
}
