package org.ragearcade.Listeners;

import net.dv8tion.jda.core.JDA;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerQuit implements Listener {

    private static JDA jda;
    private static Main plugin;

    public PlayerQuit(Main main, JDA jda) {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
        this.jda = jda;
        plugin = main;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String loadMemberSql = "SELECT * FROM RC_VerifiedMembers WHERE UUID='" + event.getPlayer().getUniqueId() + "';";
        try {
            Statement statement = Main.getConnection().createStatement();
            ResultSet members = statement.executeQuery(loadMemberSql);
            while (members.next()) {
                if (members.getString("UUID").equals(event.getPlayer().getUniqueId().toString())) {
                    Main.verifiedMembers.remove(event.getPlayer().getUniqueId());
                    Main.UUIDIdMap.remove(event.getPlayer().getUniqueId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskLater(plugin, ()->{
            Utils.sendRageEventWebhook(event.getPlayer().getName() + " has left the server!", Color.RED, jda);
            jda.getTextChannelsByName("》server-chat《", false).get(0).getManager().setTopic(Bukkit.getOnlinePlayers().size() - 1 + "/" +
                    Bukkit.getServer().getMaxPlayers() + " players online | " + Bukkit.getOfflinePlayers().length + Bukkit.getOnlinePlayers().size() + " players in total | "
                    + Main.verifiedMembers.size() + " verified members").queue();
        }, 10L);
    }
}
