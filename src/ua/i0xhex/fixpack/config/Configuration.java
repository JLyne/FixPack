package ua.i0xhex.fixpack.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

import org.bukkit.plugin.java.JavaPlugin;

import ua.i0xhex.fixpack.config.yaml.bukkit.YamlConfiguration;

public abstract class Configuration {
    
    protected JavaPlugin plugin;
    protected File file;
    protected YamlConfiguration config;
    
    /**
     * Load config from file name. File chosen from default plugin directory.
     * Resource chosen from root directory in jar.
     *
     * @param plugin plugin
     * @param name file name (ex "config.yml")
     * @param useResource copy resource or not if file not exists
     */
    public Configuration(JavaPlugin plugin, String name, boolean useResource) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name);
        if (useResource) loadConfig(name);
        else loadConfig();
    }
    
    /**
     * Load config by file without using resource.
     *
     * @param plugin plugin
     * @param file file
     */
    public Configuration(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        loadConfig();
    }
    
    /**
     * Load config from file and resource.
     * Resource used if file not exists.
     *
     * @param plugin plugin
     * @param file file
     * @param resource resource (ex "config.yml", without '/')
     */
    public Configuration(JavaPlugin plugin, File file, String resource) {
        this.plugin = plugin;
        this.file = file;
        loadConfig(resource);
    }
    
    public void loadConfig() {
        File dir = file.getParentFile();
        createDirIfNotExist(dir);
        if (!file.isFile()) throw new IllegalStateException("File " + file.getName() + " does not exist!");
        config = YamlConfiguration.loadConfiguration(file);
    }
    
    public void loadConfig(String resource) {
        File dir = file.getParentFile();
        createDirIfNotExist(dir);
        if (!file.isFile()) {
            try {
                InputStream stream = plugin.getClass().getResourceAsStream("/" + resource);
                Files.copy(stream, file.toPath());
            } catch (IOException ex) {
                throw new IllegalStateException("Could not load config " + file.getName(), ex);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    
    public void applyDefaults(String resource) {
        InputStream stream = plugin.getClass().getResourceAsStream("/" + resource);
        InputStreamReader reader = new InputStreamReader(stream);
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
        config.setDefaults(defaultConfig);
    }
    
    public void saveConfig() {
        try {
            File dir = file.getParentFile();
            createDirIfNotExist(dir);
            config.save(file);
        } catch (Exception ex) {ex.printStackTrace();}
    }
    
    // internal
    
    private void createDirIfNotExist(File dir) {
        if (!dir.isDirectory() && !dir.mkdirs())
            throw new IllegalStateException("Could not create directory " + dir.getName());
    }
}
