package ua.i0xhex.fixpack.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ua.i0xhex.fixpack.FixPack;
import ua.i0xhex.fixpack.misc.Perm;

public class CmdFixPack extends Cmd {
    public CmdFixPack(FixPack plugin) {
        super(plugin, "fixpack");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload":
                    argReload(sender, shiftArgs(args, 1));
                    return true;
            }
        }
        
        sender.sendMessage(plugin.lang().msg("command.fixpack.help"));
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        switch (args.length) {
            case 1:
                String arg = args[0];
                return Stream.of("reload")
                        .filter(a -> a.startsWith(arg)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    private void argReload(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Perm.COMMAND_FIXPACK_RELOAD)) {
            sender.sendMessage(plugin.lang().msg("command._default.no-permission"));
            return;
        }
        
        plugin.onReload();
        sender.sendMessage(plugin.lang().msg("command.fixpack.reload.success"));
    }
}
