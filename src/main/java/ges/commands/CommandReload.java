package ges.commands;

import ges.main.GraveEntitySpawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length != 1 || !args[0].equalsIgnoreCase("reload"))
        {
            sender.sendMessage("§8§l| §a/ges reload - to reload plugin's config");
            return true;
        }
        if (GraveEntitySpawn.getInstance().loadPlugin())
        {
            sender.sendMessage("§8§l| §rGraveEntitySpawn plugin was reloaded!");
        } else
        {
            sender.sendMessage("§8§l| §cPlugin was reloaded with errors... Check the console for more info!");
        }
        return true;
    }
}
