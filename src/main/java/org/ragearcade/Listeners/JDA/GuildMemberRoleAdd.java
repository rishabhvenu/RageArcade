package org.ragearcade.Listeners.JDA;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.event.EventHandler;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GuildMemberRoleAdd extends ListenerAdapter {

    private static JDA jda;

    public GuildMemberRoleAdd(JDA jda) {
        this.jda = jda;
        jda.addEventListener(this);
    }
    @EventHandler
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        String mcRole;
        for (int i = 0; i<(Utils.oldRoles.length);i++) {
            mcRole = Utils.newRoles[i];
            for (Role role: event.getRoles()) {
                Role dcRole = jda.getRolesByName(Utils.oldRoles[i], false).get(0);
                UUID playerUUID = null;
                if (dcRole == role) {
                    LuckPermsApi api = LuckPerms.getApi();
                    try {
                        Statement statement = Main.getConnection().createStatement();
                        ResultSet rs = statement.executeQuery("SELECT * FROM RC_VerifiedMembers WHERE ID='" + event.getUser().getId() + "'");
                        while (rs.next()) {
                            playerUUID = UUID.fromString(rs.getString("UUID"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if (playerUUID != null) {
                        User user = api.getUserManager().loadUser(playerUUID).join();
                        Group mcGroup = api.getGroupManager().getGroup(mcRole);
                        if (!user.inheritsGroup(mcGroup)) {
                            Node mcGroupNode = api.getNodeFactory().makeGroupNode(mcRole).build();
                            user.setPermission(mcGroupNode);
                            System.out.println(mcGroupNode);
                            api.getUserManager().saveUser(user);
                        }
                    } else {
                        System.out.println("Nani?!");
                    }

                }
            }
        }
    }
}
