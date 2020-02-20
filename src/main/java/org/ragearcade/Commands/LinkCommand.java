package org.ragearcade.Commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.util.Random;

public class LinkCommand implements CommandExecutor {

    private static JDA jda;

    public LinkCommand(JDA jda) {
        this.jda = jda;
        Bukkit.getPluginCommand("link").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Main.verifiedMembers.contains(player.getUniqueId())) {
                player.sendMessage(Utils.messageErrorPrefix + "You are already verified!");
            } else {
                if (args.length < 1) {
                    player.sendMessage(Utils.messageErrorPrefix + "You need to specify a discord username and tag! Usage: <discordid>");
                } else {
                    if (Main.guild.getMemberById(args[0]) == null) {
                        player.sendMessage(Utils.messageErrorPrefix + "You are not in the discord server, or that is an invalid discord ID!");
                    } else {
                        String randomcode = new Random().nextInt(800000) + 200000 + "RAGE";
                        Member member = Main.guild.getMemberById(args[0]);
                        Main.IDcodeMap.put(member.getUser().getId(), randomcode);
                        Main.IdUUIDMap.put(member.getUser().getId(), player.getUniqueId());
                        String tellrawtest = "[\"\",{\"text\":\"[\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/clipboard !verify " + randomcode + " \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Copy to Cliboard?\",\"color\":\"green\"}]}}},{\"text\":\"RageArcade\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/clipboard !verify " + randomcode + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Copy to Clipboard\",\"color\":\"green\"}]}}},{\"text\":\"] \",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/clipboard !verify " + randomcode + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Copy to Clipboard\",\"color\":\"green\"}]}}},{\"text\":\"Hey " + player.getName() + ", your verification code has been generated!\\nDon't share it with anyone\\nMessage RageBot#0702 in discord: !verify " + randomcode + "\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/clipboard !verify " + randomcode + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Copy to Clipboard\",\"color\":\"green\"}]}}}]";
                        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a(tellrawtest);
                        PacketPlayOutChat packet = new PacketPlayOutChat(comp);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }
        } else {
            sender.sendMessage(Utils.messageErrorPrefix + Utils.chat("You must be a player to execute this command!"));
        }
        return false;
    }
}
