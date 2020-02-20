package org.ragearcade.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ragearcade.Utils.Utils;

public class WorldTPCommand implements CommandExecutor {

    public WorldTPCommand() {
        Bukkit.getPluginCommand("wtp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("rage.wtp")) {
                if (args.length < 1) {
                    player.sendMessage(Utils.messageErrorPrefix + "You must specify a world!");
                } else {
                    World world = Bukkit.getWorld(args[0]);
                    if (world == null || !Bukkit.getWorlds().contains(world)) {
                        player.sendMessage(Utils.messageErrorPrefix + Utils.chat("&e" + args[0] + " &cis not a valid world!"));
                    } else {
                        player.teleport(world.getSpawnLocation());
                        player.sendMessage(Utils.messageSuccessPrefix + Utils.chat("Successfully teleported to world &e" + args[0]));
                    }
                }
            } else {
                player.sendMessage(Utils.messageErrorPrefix + "You don't have permission to execute this command!");
            }
        } else {
            sender.sendMessage(Utils.messageErrorPrefix + "You must be a player to execute this command!");
        }
        return false;
    }
}
