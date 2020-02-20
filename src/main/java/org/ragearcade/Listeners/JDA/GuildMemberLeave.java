package org.ragearcade.Listeners.JDA;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.ragearcade.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GuildMemberLeave extends ListenerAdapter {

    private static JDA jda;

    public GuildMemberLeave(JDA jda) {
        this.jda = jda;
        jda.addEventListener(this);
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        try {
            Statement statement = Main.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM RC_VerifiedMembers WHERE ID='" + event.getUser().getId() + "'");
            while (rs.next()) {
                Statement statement2 = Main.getConnection().createStatement();
                statement2.executeUpdate("DELETE FROM RC_VerifiedMembers WHERE ID='" + event.getUser().getId() + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
