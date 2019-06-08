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
    
    private char pathSeparator = '.';
    
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
        
        if (useResource) {
            loadConfig(name);
            applyDefaults(name);
        } else loadConfig();
    }
    
    /**
     * Load config from file name. File chosen from default plugin directory.
     * Resource chosen from root directory in jar.
     *
     * @param plugin plugin
     * @param name file name (ex "config.yml")
     * @param useResource copy resource or not if file not exists
     * @param pathSeparator path separator
     */
    public Configuration(JavaPlugin plugin, String name, boolean useResource, char pathSeparator) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name);
        this.pathSeparator = pathSeparator;
        
        if (useResource) {
            loadConfig(name);
            applyDefaults(name);
        } else loadConfig();
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
        applyDefaults(resource);
    }
    
    public void loadConfig() {
        try {
            File dir = file.getParentFile();
            createDirIfNotExist(dir);
            
            if (!file.isFile())
                throw new IllegalStateException("File " + file.getName() + " does not exist!");
    
            config = new YamlConfiguration();
            config.options().copyDefaults(true);
            config.options().pathSeparator(pathSeparator);
            
            config.load(file);
            config.save(file);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load config: " + file.getName(), ex);
        }
    }
    
    public void loadConfig(String resource) {
        try {
            File dir = file.getParentFile();
            createDirIfNotExist(dir);
    
            config = new YamlConfiguration();
            config.options().copyDefaults(true);
            config.options().pathSeparator(pathSeparator);
            
            if (!file.isFile()) {
                InputStream stream = plugin.getClass().getResourceAsStream("/" + resource);
                config.load(new InputStreamReader(stream));
            } else config.load(file);
    
            config.save(file);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load config with resource: " + resource, ex);
        }
    }
    
    public void applyDefaults(String resource) {
        try {
            InputStream stream = plugin.getClass().getResourceAsStream("/" + resource);
            InputStreamReader reader = new InputStreamReader(stream);
            
            YamlConfiguration defaults = new YamlConfiguration();
            defaults.options().pathSeparator(pathSeparator);
            defaults.load(reader);
            
            config.setDefaults(defaults);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to apply defaults: " + resource, ex);
        }
    }
    
    public void saveConfig() {
        try {
            File dir = file.getParentFile();
            createDirIfNotExist(dir);
            config.save(file);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    // internal
    
    private void createDirIfNotExist(File dir) {
        if (!dir.isDirectory() && !dir.mkdirs())
            throw new IllegalStateException("Could not create directory " + dir.getName());
    }
}
