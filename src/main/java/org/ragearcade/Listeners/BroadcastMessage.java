package org.ragearcade.Listeners;

import java.awt.Color;
import net.dv8tion.jda.core.JDA;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

public class BroadcastMessage implements Listener {

    private static JDA jda;

    public BroadcastMessage(Main main, JDA jda) {
        Bukkit.getPluginManager().registerEvents(this, main);
        this.jda = jda;
    }
    @EventHandler
    public void onBroadcastmessage(BroadcastMessageEvent event) {
        String message = event.getMessage();
        for (String s: Utils.colorCodes) {
            message = message.replace(s, "");
        }
        Utils.sendServerMessageWebhook(message, jda);
    }
}
