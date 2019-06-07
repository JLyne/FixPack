package ua.i0xhex.fixtools.config;

import ua.i0xhex.fixtools.FixTools;

public class Config extends Configuration {
    public Config(FixTools plugin) {
        super(plugin, "config.yml", true);
        applyDefaults("config.yml");
    }
}
