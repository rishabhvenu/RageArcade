package org.ragearcade.Listeners.JDA;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.sql.SQLException;
import java.sql.Statement;

public class PrivateMessageReceived extends ListenerAdapter {
    public PrivateMessageReceived(JDA jda) {
        jda.addEventListener(this);
    }
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase("!verify")) {
            if (args.length < 2) {
                event.getChannel().sendMessage(":x: You have to specify your verification code!").queue();
            } else {
                if (Main.IdUUIDMap.containsKey(event.getAuthor().getId())) {
                    if (Main.guild.getMemberById(event.getAuthor().getId()) == null) {
                        event.getChannel().sendMessage(":x: You are not even in the discord server!").queue();
                    } else {
                        String actualCode = Main.IDcodeMap.get(event.getAuthor().getId());
                        if (actualCode.equals(args[1])) {
                            Main.verifiedMembers.add(Main.IdUUIDMap.get(event.getAuthor().getId()));
                            Main.IDcodeMap.remove(event.getAuthor().getId());
                            Statement addVerified = null;
                            try {
                                addVerified = Main.getConnection().createStatement();
                                addVerified.executeUpdate("INSERT INTO RC_VerifiedMembers (UUID, ID) VALUES ('" + Main.IdUUIDMap.get(event.getAuthor().getId())
                                        + "', '" + event.getAuthor().getId() + "')");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Role verifiedRole = Main.guild.getRolesByName("Verified", false).get(0);
                            Role unverifiedRole = Main.guild.getRolesByName("Unverified", false).get(0);
                            Main.guild.getController().addSingleRoleToMember(Main.guild.getMemberById(event.getAuthor().getId()), verifiedRole).queue();
                            Main.guild.getController().removeSingleRoleFromMember(Main.guild.getMemberById(event.getAuthor().getId()), unverifiedRole).queue();
                            if (Bukkit.getPlayer(Main.IdUUIDMap.get(event.getAuthor().getId())) != null) {
                                Bukkit.getPlayer(Main.IdUUIDMap.get(event.getAuthor().getId()))
                                        .sendMessage(Utils.messageSuccessPrefix + Utils.chat("Your account has been successfully verified! You linked your" +
                                                " account with member: " + Main.guild.getMemberById(event.getAuthor().getId()).getUser().getName()
                                                + "#" + Main.guild.getMemberById(event.getAuthor().getId()).getUser().getDiscriminator()));
                               event.getChannel().sendMessage(":white_check_mark: Your account has been successfully verified!" +
                                        "You link you account with minecraft account: " + Bukkit.getPlayer(Main.IdUUIDMap.get(event.getAuthor().getId())).getName()).queue();
                            } else {
                                event.getChannel().sendMessage(":white_check_mark: Your account has been successfully verified!" +
                                        "You link you account with minecraft account: " + Bukkit.getOfflinePlayer(Main.IdUUIDMap.get(event.getAuthor().getId())).getName()).queue();
                            }
                            Main.IdUUIDMap.remove(event.getAuthor().getId());
                        } else {
                            event.getChannel().sendMessage(":x: Invalid Code! Check Minecraft again!").queue();
                        }
                    }
                } else {
                    event.getChannel().sendMessage(":x: No pending verification process!").queue();
                }
            }
        }
    }
}
