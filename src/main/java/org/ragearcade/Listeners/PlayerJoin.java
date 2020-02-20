package org.ragearcade.Listeners;

import net.dv8tion.jda.core.JDA;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerJoin implements Listener {

    private static JDA jda;

    public PlayerJoin(Main main, JDA jda) {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
        this.jda = jda;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String loadMemberSql = "SELECT * FROM RC_VerifiedMembers WHERE UUID='" + event.getPlayer().getUniqueId() + "';";
        int verifiedMembers = 0;
        try {
            Statement statement = Main.getConnection().createStatement();
            ResultSet members = statement.executeQuery(loadMemberSql);
            while (members.next()) {
                if (members.getString("UUID").equals(event.getPlayer().getUniqueId().toString())) {
                    Main.verifiedMembers.add(event.getPlayer().getUniqueId());
                    Main.UUIDIdMap.put(event.getPlayer().getUniqueId(), members.getString("ID"));
                }
            }
            Statement statement2 = Main.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs2 = statement2.executeQuery("SELECT * FROM RC_VerifiedMembers");
            rs2.last();
            verifiedMembers = rs2.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!event.getPlayer().hasPlayedBefore()) {
            Utils.sendRageEventWebhook(":tada: Welcome " + event.getPlayer().getName() + " to the server!", Color.PINK, jda);
        } else {
            Utils.sendRageEventWebhook(event.getPlayer().getName() + " has joined the server!", Color.GREEN, jda);
        }
        jda.getTextChannelsByName("》server-chat《", false).get(0).getManager().setTopic(Bukkit.getOnlinePlayers().size() + "/" +
                Bukkit.getServer().getMaxPlayers() + " players online | " + Bukkit.getOfflinePlayers().length + Bukkit.getOnlinePlayers().size() + " players in total | "
                + verifiedMembers + " verified members").queue();
    }
}
