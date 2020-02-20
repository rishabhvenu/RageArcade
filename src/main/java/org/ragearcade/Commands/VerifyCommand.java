package org.ragearcade.Commands;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class VerifyCommand implements CommandExecutor {

    public VerifyCommand() {
        Bukkit.getPluginCommand("verify").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!Main.verifiedMembers.contains(player.getUniqueId())) {
                if (args.length < 1) {
                    player.sendMessage(Utils.messageErrorPrefix + Utils.chat("You need to specify your verification code!\nUsage: /verify <code>"));
                    return true;
                } else {
                    if (!Main.UUIDCodeMap.containsKey(player.getUniqueId())) {
                        player.sendMessage(Utils.messageErrorPrefix + Utils.chat("Not pending verification process!"));
                        return true;
                    } else {
                        String actualCode = Main.UUIDCodeMap.get(player.getUniqueId());
                        if (!actualCode.equals(args[0])) {
                            player.sendMessage(Utils.messageErrorPrefix + Utils.chat("Invalid code! Check your private message again!"));
                            return true;
                        } else {
                            String discordId = Main.dcUUIDIdMap.get(player.getUniqueId());
                            Member member = Main.guild.getMemberById(discordId);
                            Main.UUIDCodeMap.remove(player.getUniqueId());
                            Main.dcUUIDIdMap.remove(player.getUniqueId());
                            if (member == null) {
                                player.sendMessage(Utils.messageErrorPrefix + Utils.chat("It looks like you left the discord server!"));
                                return true;
                            } else {
                                Main.verifiedMembers.add(player.getUniqueId());
                                try {
                                    for (UUID uuid : Main.verifiedMembers) {
                                        Statement checkIfExist = Main.getConnection().createStatement();
                                        ResultSet members = checkIfExist.executeQuery("SELECT * FROM RC_VerifiedMembers WHERE UUID='" + uuid + "';");
                                        if (!members.next()) {
                                            Statement addVerified = Main.getConnection().createStatement();
                                            addVerified.executeUpdate("INSERT INTO RC_VerifiedMembers (UUID, ID) VALUES ('" + uuid + "', '" + discordId + "')");
                                        }
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                Role verifiedRole = Main.guild.getRolesByName("Verified", false).get(0);
                                Role unverifiedRole = Main.guild.getRolesByName("Unverified", false).get(0);
                                Main.guild.getController().addSingleRoleToMember(member, verifiedRole).queue();
                                Main.guild.getController().removeSingleRoleFromMember(member, unverifiedRole).queue();
                                member.getUser().openPrivateChannel().complete().sendMessage(":white_check_mark: Your account has been successfully verified!" +
                                        "You link you account with minecraft account: " + player.getName()).queue();
                                player.sendMessage(Utils.messageSuccessPrefix + Utils.chat("Your account has been successfully verified! You linked your" +
                                        " account with member: " + member.getUser().getName() + "#" + member.getUser().getDiscriminator()));
                            }
                        }
                    }
                }
            } else {
                player.sendMessage(Utils.messageErrorPrefix + Utils.chat("Your account is already verified!"));
            }
        } else {
            sender.sendMessage(Utils.messageErrorPrefix + Utils.chat("Only players can execute this command!"));
        }
        return false;
    }
}
