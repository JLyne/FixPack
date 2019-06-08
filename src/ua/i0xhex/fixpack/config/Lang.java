package ua.i0xhex.fixpack.config;

import java.util.*;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Language configuration based on YAML
 * @author i0xHeX
 */
public class Lang extends Configuration {
    
    private Map<String, String> messageMap;
    
    /**
     * Init lang config from `lang.yml` file and load data.
     *
     * @param plugin plugin
     */
    public Lang(JavaPlugin plugin) {
        super(plugin, "lang.yml", true, '#');
        messageMap = new HashMap<>();
        for (String key : config.getKeys(true))
            messageMap.put(key, config.getString(key));
    }
    
    /**
     * Return optional message by id
     *
     * @param id message id
     * @return optional message
     */
    public Optional<String> get(String id) {
        return Optional.ofNullable(messageMap.get(id));
    }
    
    /**
     * Return message by id or default if not exist.
     *
     * @param id message id
     * @param defaultMessage default message
     * @return message
     */
    public String get(String id, String defaultMessage) {
        return messageMap.getOrDefault(id, defaultMessage);
    }
    
    /**
     * Return message by id or "§4[ID]§r" if not exist.
     *
     * @param id message id
     * @return message
     */
    public String msg(String id) {
        return get(id, "§4[" + id + "]§r");
    }
    
    /**
     * Return message by id and placeholders.<br>
     * First argument is message with placeholders (ex "{player} got {amount} for {price}").
     * Second argument is placeholder names (ex "{player} {amount} {price}").
     * Third argument is data for each placeholder (three objects).
     *
     * @param id message id
     * @param placeholders placeholders
     * @param data data
     * @return message
     */
    public String msg(String id, String placeholders, Object ... data) {
        String message = get(id, null);
        if (message == null) return "§4[" + id + "]§r";
        if (message.isEmpty()) return message;
        
        String[] placeholdersArr = placeholders.split(" ");
        if (placeholdersArr.length != data.length)
            throw new IllegalArgumentException("Placeholders and data size should be equal.");
        for (int i = 0; i < data.length; i++)
            message = message.replace(placeholdersArr[i], String.valueOf(data[i]));
        return message;
    }
    
    /**
     * Return message by id and placeholders.
     *
     * @param id id
     * @param placeholders placeholder list
     * @param data data list
     * @return message
     */
    public String msg(String id, List<String> placeholders, List<Object> data) {
        String message = get(id, null);
        if (message == null) return "§4[" + id + "]§r";
        if (message.isEmpty()) return message;
        
        if (placeholders.size() != data.size())
            throw new IllegalArgumentException("Placeholders and data size should be equal.");
        for (int i = 0; i < data.size(); i++)
            message = message.replace(placeholders.get(i), String.valueOf(data.get(i)));
        return message;
    }
    
    /**
     * Send message to console or player, if not empty.
     *
     * @param sender target
     * @param id message id
     */
    public void sendMessage(CommandSender sender, String id) {
        String message = msg(id);
        if (!message.isEmpty()) sender.sendMessage(message);
    }
    
    /**
     * Send message to console or player, if not empty.
     *
     * @param sender target
     * @param id message id
     * @param placeholders placeholders
     * @param data data
     */
    public void sendMessage(CommandSender sender, String id, String placeholders, Object ... data) {
        String message = msg(id, placeholders, data);
        if (!message.isEmpty()) sender.sendMessage(message);
    }
    
    /**
     * Send message to console or player, if not empty.
     *
     * @param sender target
     * @param id message id
     * @param placeholders placeholders
     * @param data data
     */
    public void sendMessage(CommandSender sender, String id, List<String> placeholders, List<Object> data) {
        String message = msg(id, placeholders, data);
        if (!message.isEmpty()) sender.sendMessage(message);
    }
    
    /**
     * Splits the message by multiple lines to list
     *
     * @param message message (formatted / translated)
     * @return message as list
     */
    public List<String> list(String message) {
        return Arrays.asList(message.split("\n"));
    }
}