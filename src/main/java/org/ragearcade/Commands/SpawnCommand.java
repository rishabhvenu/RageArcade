package org.ragearcade.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ragearcade.Utils.Utils;

public class SpawnCommand implements CommandExecutor {

    public SpawnCommand() {
        Bukkit.getPluginCommand("spawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.teleport(new Location(Bukkit.getWorld("world"), -910.975, 9.00000, 1808.008));
        } else {
            sender.sendMessage(Utils.messageErrorPrefix + Utils.chat("You must be a player to execute this command!"));
        }
        return false;
    }
}
