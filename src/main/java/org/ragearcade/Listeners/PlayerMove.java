package org.ragearcade.Listeners;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

public class PlayerMove implements Listener {

    public PlayerMove(Main main) {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        int coordx = event.getPlayer().getLocation().getBlockX();
        int coordy = event.getPlayer().getLocation().getBlockY();
        int coordz = event.getPlayer().getLocation().getBlockZ();
        if (coordx == 213) {
            World world = event.getPlayer().getWorld();
            if (world.getBlockAt(212, coordy+1, coordz).equals(Material.PURPLE_STAINED_GLASS_PANE)
                    || world.getBlockAt(212, coordy+1, coordz).equals(Material.MAGENTA_STAINED_GLASS_PANE)
                    || world.getBlockAt(212, coordy+1, coordz).equals(Material.PINK_STAINED_GLASS_PANE)) {
                LuckPermsApi api = LuckPerms.getApi();
                User user = api.getUserManager().loadUser(event.getPlayer().getUniqueId()).join();
                boolean hasRank = false;
                for (String rank: Utils.ranks) {
                    hasRank = true;
                    Group group = api.getGroupManager().getGroup(rank);
                    if (user.inheritsGroup(group)) {
                        Bukkit.dispatchCommand(event.getPlayer(), "warp " + rank);
                    }
                }
                if (!hasRank) {
                    event.getPlayer().sendMessage(Utils.messageErrorPrefix + "You are not in a mine, Do /rankup to start!");
                }
            }
        }
    }
}
