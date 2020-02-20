package org.ragearcade.Utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.webhook.WebhookClient;
import net.dv8tion.jda.webhook.WebhookClientBuilder;
import net.dv8tion.jda.webhook.WebhookMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Utils {
    public static String messageErrorPrefix = chat("&6&lRageArcade &7&l>&c ");
    public static String messageSuccessPrefix = chat("&6&lRageArcade &7&l>&a ");
    public static Material[] items = {Material.STONE, Material.COBBLESTONE,  Material.COAL, Material.COAL_BLOCK, Material.IRON_INGOT, Material.IRON_BLOCK,
            Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, Material.GOLD_INGOT, Material.GOLD_BLOCK, Material.DIAMOND, Material.DIAMOND_BLOCK,
            Material.EMERALD, Material.EMERALD_BLOCK};
    public static Material[] itemBlocks = {Material.STONE, Material.COBBLESTONE,  Material.COAL_ORE, Material.COAL_BLOCK, Material.IRON_ORE, Material.IRON_BLOCK,
            Material.LAPIS_ORE, Material.LAPIS_BLOCK, Material.GOLD_ORE, Material.GOLD_BLOCK, Material.DIAMOND_ORE, Material.DIAMOND_BLOCK,
            Material.EMERALD_ORE, Material.EMERALD_BLOCK};
    public static Material[] tools = {Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE};
    public static String[] toolDisplayNames = {"&aWood Pickaxe", "&8Stone Pickaxe", "&fIron Pickaxe", "&bDiamond Pickaxe"};
    public static String[] toolCosts = {"&5Cost: $50", "&5Cost: $500", "&5Cost: $925", "&5Cost: $1500"};
    public static String[] toolLoreSell = {"&5Sell Price: $40", "&5Sell Price: $400", "&5Sell Price: $740", "&5Sell Price: $1200"};
    public static String[] oreDisplayNames = {"&7Stone", "&7Cobblestone", "&8Coal Ore", "&8Coal Block", "&fIron Ore", "&fIron Block", "&1Lapis Ore", "&1Lapis Block",
            "&6Gold Ore", "&6Gold Block", "&bDiamond Ore", "&bDiamond Block", "&2Emerald Ore", "&2Emerald Block"};
    public static String[] oreCosts = {"&5Cost: $15", "&5Cost: $15", "&5Cost: $50", "&5Cost: $500", "&5Cost: $80", "&5Cost: $800", "&5Cost: $85", "&5Cost: $850",
            "&5Cost: $115", "&5Cost: $1150", "&5Cost: $150", "&5Cost: $1500", "&5Cost: $260", "&5Cost: $2600"};
    public static String[] oreLoreSell = {"&5Sell Price: $12", "&5Sell Price: $12", "&5Sell Price: $40", "&5Sell Price: $400", "&5Sell Price: $64", "&5Sell Price: $640",
            "&5Sell Price: $68", "&5Sell Price: $680", "&5Sell Price: $92", "&5Sell Price: $920", "&5Sell Price: $120", "&5Sell Price: $1200", "&5Sell Price: $208",
            "&5Sell Price: $2080"};
    public static String[] oldRoles = {"》》Owner《《", "》》Developer《《", "》》Manager《《", "》》Admin《《", "》》Moderator《《", "》》Helper《《", "》》Staff《《",
            "👷 Builder 👷", "》》Youtuber《《", "👴 Retired 👴", "💸Donator(100+)💸",
            "💸Donator(50+)💸", "💸Donator(20+)💸", "💸Donator(5+)💸", "⛏ Inmate ⛏", "Verified", "Unverified"};
    public static String[] newRoles = {"owner", "developer", "manager", "admin", "moderator", "helper", "staff", "builder", "youtuber", "retired", "donator",
            "donator", "donator", "donator", "default", "verified", "unverified"};
    public static String[] colorCodes = {"§4", "§c", "§6", "§e", "§2", "§a", "§b", "§3", "§1", "§9", "§d", "§5", "§f", "§7", "§8", "§0", "§r"};
    public static String[] ranks = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static String chat (String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static ItemStack createItem(Inventory inv, Material mat, int amount, int invSlot, String displayName, String... loreString) {
        ItemStack item;
        List<String> lore = new ArrayList<>();
        item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(chat(displayName));
        for (String s: loreString) {
            lore.add(chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot-1, item);
        return item;
    }
    public static void openShopGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, chat("&5Item Shop"));
        createItem(inv, Material.DIAMOND, 1, 12, "&dOre Shop", "&5You can buy ores here!");
        createItem(inv, Material.IRON_PICKAXE, 1,14, "&dTool Shop", "&5You can buy tools here!");
        player.openInventory(inv);
    }
    public static void openOreShopGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, chat("&5Ore Shop"));
        int[] slots = {11, 12, 13, 14, 15, 16, 17, 20, 21, 22, 23, 24, 25, 26, 29};
        for (int i = 0;i<(items.length);i++) {
            createItem(inv, items[i], 1, slots[i], oreDisplayNames[i], oreCosts[i], oreLoreSell[i]);
        }
        createItem(inv, Material.ARROW, 1, 46, "&8Go Back");
        player.openInventory(inv);
    }
    public static void openToolShopGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, chat("&5Tool Shop"));
        int[] slots = {11, 12, 13, 14};
        for (int i = 0;i<(tools.length);i++) {
            createItem(inv, tools[i], 1, slots[i], toolDisplayNames[i], toolCosts[i], toolLoreSell[i]);
        }
        createItem(inv, Material.ARROW, 1, 46, "&8Go Back");
        player.openInventory(inv);
    }
    public static void openConfirmBuyOreItem(Material material, String displayName, Player player, String lore, boolean buy) {
        Inventory inv;
        if (buy) {
            inv = Bukkit.createInventory(null, 54, Utils.chat("&5Confirm Buy"));
        } else {
            inv = Bukkit.createInventory(null, 54, Utils.chat("&5Confirm Sell"));
        }
        createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 19, "&aAdd 1");
        createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 10, 20, "&aAdd 10");
        createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 64, 21, "&aAdd 64");
        createItem(inv, Material.BARRIER, 1, 14, "&cCancel");
        createItem(inv, material, 1, 23, displayName, lore);
        if (buy) {
            createItem(inv, Material.GREEN_TERRACOTTA, 1, 32, "&aBuy");
        } else {
            createItem(inv, Material.RED_TERRACOTTA, 1, 32, "&cSell");
        }
        createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 25, "&cRemove 1");
        createItem(inv, Material.RED_STAINED_GLASS_PANE, 10, 26, "&cRemove 10");
        createItem(inv, Material.RED_STAINED_GLASS_PANE, 64, 27, "&cRemove 64");
        createItem(inv, Material.ARROW, 1, 46, "&8Go Back");
        player.openInventory(inv);
    }
    public static void sendRageEventWebhook(String message, Color color, JDA jda) {
        EmbedBuilder EB = new EmbedBuilder();
        EB.setTitle(message);
        EB.setColor(color);
        TextChannel server_chat = jda.getTextChannelsByName("》server-chat《", false).get(0);
        final Webhook webhook = server_chat.createWebhook("RageArcade").complete();
        WebhookClientBuilder clientBuilder = webhook.newClient();
        final WebhookClient client = clientBuilder.build();
        WebhookMessageBuilder wbm = new WebhookMessageBuilder();
        wbm.setUsername("RageEvents");
        wbm.setAvatarUrl("https://gamepedia.cursecdn.com/minecraft_gamepedia/thumb/4/44/Grass_Block_Revision_6.png/225px-Grass_Block_Revision_6.png?version=81d592628aa6663003878ff3afecd2a4");
        wbm.addEmbeds(EB.build());
        client.send(wbm.build()).thenRun(()->{
            client.close();
            webhook.delete().complete();
        });
    }
    public static void sendServerMessageWebhook(String message, JDA jda) {
        TextChannel server_chat = jda.getTextChannelsByName("》server-chat《", false).get(0);
        final Webhook webhook = server_chat.createWebhook("RageArcade").complete();
        WebhookClientBuilder clientBuilder = webhook.newClient();
        final WebhookClient client = clientBuilder.build();
        WebhookMessageBuilder wbm = new WebhookMessageBuilder();
        wbm.setUsername("RageEvents");
        wbm.setAvatarUrl("https://gamepedia.cursecdn.com/minecraft_gamepedia/thumb/4/44/Grass_Block_Revision_6.png/225px-Grass_Block_Revision_6.png?version=81d592628aa6663003878ff3afecd2a4");
        wbm.setContent(message);
        String lastMessage = server_chat.getMessageById(server_chat.getLatestMessageId()).toString();
                client.send(wbm.build()).thenRun(() -> {
                    client.close();
                    webhook.delete().complete();
                });
        }
    public static void sendVerificationWebhook(String message, Color color, JDA jda) {
        EmbedBuilder EB = new EmbedBuilder();
        EB.setTitle(message);
        EB.setColor(color);
        TextChannel verification_chat = jda.getTextChannelsByName("》chat《", false).get(0);
        final Webhook webhook = verification_chat.createWebhook("RageArcade").complete();
        WebhookClientBuilder clientBuilder = webhook.newClient();
        final WebhookClient client = clientBuilder.build();
        WebhookMessageBuilder wbm = new WebhookMessageBuilder();
        wbm.setUsername("RageVerification");
        wbm.setAvatarUrl("https://image.flaticon.com/icons/svg/21/21104.svg");
        wbm.addEmbeds(EB.build());
        client.send(wbm.build()).thenRun(()->{
            client.close();
            webhook.delete().complete();
        });
    }
}
