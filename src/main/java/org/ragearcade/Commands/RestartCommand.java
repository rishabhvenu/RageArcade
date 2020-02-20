package org.ragearcade.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

public class RestartCommand implements CommandExecutor {

    private static Main plugin;

    public RestartCommand(Main main) {
        main.getCommand("restart").setExecutor(this);
        plugin = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("rage.restart")) {
            Main.lastExecutedCommand = "/restart";
            Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.spigot().restart(), 20L);
        } else {
            commandSender.sendMessage(Utils.messageErrorPrefix + "You don't have permission to execute this command!");
        }
        return false;
    }
}
