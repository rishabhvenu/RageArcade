package org.ragearcade.Listeners.JDA;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class GuildMessageReceived extends ListenerAdapter {

    private static JDA jda = null;

    public GuildMessageReceived(JDA jda) {
        jda.addEventListener(this);
        this.jda = jda;
    }

    String[] newD2MRoles = {"&8(&cOWNER&8)&c", "&8(&3DEVELOPER&8)&3", "&8(&eMANAGER&8)&e", "&8(&1ADMIN&8)&1", "&8(&2MODERATOR&8)&2", "&8(&5HELPER&8)&5", "&8(&aSTAFF&8)&a",
            "&8(&bBUILDER&8)&b", "&8(&cYOUTUBER&8)&c", "&8(&4RETIRED&8)&4", "&8(&dDONATOR&8)&d", "&8(&dDONATOR&8)&d", "&8(&dDONATOR&8)&d", "&8(&dDONATOR&8)&d",
             "&8(&7INMATE&8)&7", "Verified", "Unverified"};
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot() || user.isFake() || event.isWebhookMessage()) {
            return;
        } else {
            if (event.getChannel() == jda.getTextChannelsByName("》server-chat《", true).get(0)) {
                String message = event.getMessage().getContentRaw();
                String roleName = null;
                for (int i = 0; i < (Utils.oldRoles.length); i++) {
                    for (Role r: event.getMember().getRoles()) {
                        if (r.getName().equals(Utils.oldRoles[i]) && !(newD2MRoles[i].equals("&8(&aSTAFF&8)&a") || newD2MRoles[i].equals("Verified")
                                || newD2MRoles[i].equals("Unverified"))) {
                            roleName = newD2MRoles[i];
                            break;
                        }
                    }
                }
                String userName = user.getName();
                try {
                    Statement statement = Main.getConnection().createStatement();
                    ResultSet rs = statement.executeQuery("SELECT * FROM RC_VerifiedMembers WHERE ID='" + user.getId() + "'");
                    while (rs.next()) {
                        UUID playerUUID = UUID.fromString(rs.getString("UUID"));
                        if (Bukkit.getPlayer(playerUUID) == null) {
                            userName = Bukkit.getOfflinePlayer(playerUUID).getName();
                        } else {
                            userName = Bukkit.getPlayer(playerUUID).getName();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (User mentionedUser: event.getMessage().getMentionedUsers()) {
                    String mentionString = "<@" + mentionedUser.getId() + ">";
                    message = message.replace(mentionString, Utils.chat("&3@" + mentionedUser.getName() + "&r"));
                    mentionString = "<@!" + mentionedUser.getId() + ">";
                    message = message.replace(mentionString, Utils.chat("&3@" + mentionedUser.getName() + "&r"));
                }
                for (Channel mentionedChannel: event.getMessage().getMentionedChannels()) {
                    String mentionString = "<#" + mentionedChannel.getId() + ">";
                    message = message.replace(mentionString, Utils.chat("&3#" + mentionedChannel.getName() + "&r"));
                }
                for (Role mentionedRole: event.getMessage().getMentionedRoles()) {
                    String mentionString = "<@&" + mentionedRole.getId() + ">";
                    message = message.replace(mentionString, Utils.chat("&3@" + mentionedRole.getName() + "&r"));
                }
                for (Player player: Bukkit.getOnlinePlayers()) {
                    player.sendMessage(Utils.chat(roleName + " " + userName + "&r: " + message));
                }
            }
            String[] args = event.getMessage().getContentRaw().split(" ");
            if (args[0].equals("!link")) {
                if (event.getMember().getRoles().stream().filter(role -> role.getName().equals("Verified")).findAny().orElse(null) != null) {
                    Utils.sendVerificationWebhook("Your minecraft account is already linked!", Color.RED, jda);
                } else {
                    if (Main.dcUUIDIdMap.values().contains(event.getAuthor().getId())) {

                    } else {
                        if (args.length < 2) {
                            Utils.sendVerificationWebhook("You need to specify your in-game username!", Color.RED, jda);
                            return;
                        } else {
                            Player player = Bukkit.getPlayerExact(args[1]);
                            if (player == null) {
                                Utils.sendVerificationWebhook("That player is not online and/or doesn't exist!", Color.RED, jda);
                                return;
                            } else {
                                String randomcode = new Random().nextInt(800000) + 200000 + "RAGE";
                                Main.UUIDCodeMap.put(player.getUniqueId(), randomcode);
                                Main.dcUUIDIdMap.put(player.getUniqueId(), user.getId());
                                user.openPrivateChannel().complete().sendMessage("Hey " + user.getName() + ", your verification code has been generated!\n" +
                                        "Don't share it with anyone!\n" +
                                        "Type this command in game: `/verify " + randomcode + "`").queue();
                            }
                        }
                    }
                }
            }
        }
    }
}
