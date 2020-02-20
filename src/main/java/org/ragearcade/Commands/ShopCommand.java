package org.ragearcade.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.ragearcade.Utils.Utils;

public class ShopCommand implements CommandExecutor {
    public ShopCommand() {
        Bukkit.getPluginCommand("shop").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Utils.openShopGUI(player);
        } else {
            ConsoleCommandSender cmdsender = (ConsoleCommandSender) sender;
            cmdsender.sendMessage(Utils.messageErrorPrefix + Utils.chat("&cYou must be a player to execute this command!"));
        }
        return false;
    }
}
