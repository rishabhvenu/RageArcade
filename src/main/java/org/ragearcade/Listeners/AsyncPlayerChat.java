package org.ragearcade.Listeners;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;
import net.dv8tion.jda.webhook.WebhookMessage;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

public class AsyncPlayerChat implements Listener {

    private static JDA jda;

    public AsyncPlayerChat(Main main, JDA jda) {
        this.jda = jda;
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        TextChannel server_chat = jda.getTextChannelsByName("》server-chat《", true).get(0);
        final Webhook webhook = server_chat.createWebhook("RageArcade").complete();
        WebhookClientBuilder clientBuilder = webhook.newClient();
        final WebhookClient client = clientBuilder.build();
        WebhookMessageBuilder wbm = new WebhookMessageBuilder();
        String playerName = event.getPlayer().getDisplayName();
        for (String s: Utils.colorCodes) {
            playerName = playerName.replace(s, "");
            message = message.replace(s, "");
        }
        wbm.setContent(message);
        wbm.setUsername(playerName);
        if (playerName.equals("(OWNER) TheOriginalAse")) {
            wbm.setAvatarUrl("https://minotar.net/helm/TheOriginalAse");
        } else {
            wbm.setAvatarUrl("https://minotar.net/avatar/" + event.getPlayer().getUniqueId());
        }
        WebhookMessage webhookMessage = wbm.build();
        if (!event.isCancelled()) {
            try {
                client.send(webhookMessage).thenRun(() -> {
                    client.close();
                    webhook.delete().complete();
                });
            } catch (ErrorResponseException e) {
                for (Webhook webhook2: server_chat.getWebhooks().complete()) {
                    webhook2.delete().complete();
                }
            }
        }
        String[] messageArr = event.getMessage().split("\\s+");
        for (Player p: Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < (messageArr.length); i++) {
                if (messageArr[i].toLowerCase().contains(p.getName().toLowerCase())) {
                    event.getPlayer().getWorld().playSound(Bukkit.getPlayerExact(p.getName()).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP
                            , 1, (float) 1);
                    messageArr[i] = messageArr[i].replace(p.getName(), Utils.chat("&5" + p.getName() + "&r"));
                }
            }
        }
        event.setMessage(String.join(" ", messageArr));
    }
}
