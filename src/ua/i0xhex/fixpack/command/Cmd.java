package ua.i0xhex.fixpack.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.*;

import ua.i0xhex.fixpack.FixPack;

public abstract class Cmd implements CommandExecutor, TabCompleter {
    
    protected FixPack plugin;
    
    public Cmd(FixPack plugin, String name) {
        this.plugin = plugin;
    
        PluginCommand command = plugin.getCommand(name);
        if (command == null) {
            Bukkit.getLogger().warning(String.format(
                    "[FixPack] Command '%s' not found in plugin.yml!",
                    name));
        } else {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return Collections.emptyList();
    }
    
    protected String[] shiftArgs(String[] args, int pos) {
        if (args.length == 1) return new String[0];
        else return Arrays.copyOfRange(args, pos, args.length);
    }
}
