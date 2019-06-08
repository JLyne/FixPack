package ua.i0xhex.fixpack.fix.villager;

import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftVillager;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;

import com.google.common.collect.Sets;

import ua.i0xhex.fixpack.FixPack;
import ua.i0xhex.fixpack.Manager;
import ua.i0xhex.fixpack.config.Config;
import ua.i0xhex.fixpack.fix.VillagerFixManager;

import net.minecraft.server.v1_14_R1.*;

public class VillagerLockRandomTrades implements Listener, Manager {
    
    private FixPack plugin;
    private VillagerFixManager manager;
    
    private boolean enabled;
    
    public VillagerLockRandomTrades(FixPack plugin, VillagerFixManager manager) {
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
    
    @EventHandler
    public void onVillagerChangeWork(VillagerCareerChangeEvent e) {
        if (enabled) {
            if (e.getReason() == VillagerCareerChangeEvent.ChangeReason.LOSING_JOB) return;
            Villager villager = e.getEntity();
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                Random random = new Random(villager.getUniqueId().hashCode());
                
                EntityVillager evillager = ((CraftVillager) villager).getHandle();
                VillagerData vdata = evillager.getVillagerData();
                Int2ObjectMap<VillagerTrades.IMerchantRecipeOption[]> tradesMap = VillagerTrades.a.get(vdata.getProfession());
                
                if (tradesMap != null && !tradesMap.isEmpty()) {
                    VillagerTrades.IMerchantRecipeOption[] recipeOptions = tradesMap.get(vdata.getLevel());
                    if (recipeOptions != null) {
                        int i = 2;
                        MerchantRecipeList recipeList = evillager.getOffers();
                        Set<Integer> integers = Sets.newHashSet();
                        
                        if (recipeOptions.length > i)
                            while (integers.size() < i)
                                integers.add(random.nextInt(recipeOptions.length));
                        else
                            for (int j = 0; j < recipeOptions.length; ++j)
                                integers.add(j);
                        
                        for (int integer : integers) {
                            VillagerTrades.IMerchantRecipeOption recipeOption = recipeOptions[integer];
                            MerchantRecipe merchantrecipe = recipeOption.a(evillager, random);
                            recipeList.add(merchantrecipe);
                        }
                    }
                }
            });
        }
    }
    
    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent e) {
        if (enabled) {
            AbstractVillager abstractVillager = e.getEntity();
            if (!(abstractVillager instanceof Villager)) return;
            Villager villager = (Villager) abstractVillager;
            
            if (villager.getVillagerLevel() == 1 && villager.getVillagerExperience() == 0)
                e.setCancelled(true); // we handle that self
        }
    }
    
    private void loadConfig() {
        if (!plugin.version().equals("v1_14_R1")) {
            Bukkit.getLogger().info("" +
                    "[FixPack] Feature 'villager::lock-random-trades' " +
                    "disabled due to unsupported version. Supported version: 1.14.2");
            return;
        }
        
        Config config = plugin.config();
        enabled = config.villagerLockRandomTrades();
    }
}
