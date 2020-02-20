package org.ragearcade.Listeners;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ragearcade.Main;

import java.util.ArrayList;
import java.util.List;

public class EntityPickupItem implements Listener {

    private static Economy econ;

    public EntityPickupItem(Main main, Economy economy) {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
        econ = economy;
    }
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        List<String> lore = item.getItemMeta().getLore();
        boolean onCooldown = false;
        boolean isCoin = false;
        int slotatwhich = 0;
        if (item.getItemMeta().hasLore()) {
            for (int i=0;i<(lore.size());i++) {
                if (lore.get(i).startsWith("Player: ")) {
                    slotatwhich = i;
                    onCooldown = true;
                    break;
                } else if (lore.get(i).startsWith("Coin: ")) {
                    slotatwhich = i;
                    isCoin = true;
                    break;
                }
            }
            if (onCooldown) {
                String[] lores = lore.get(slotatwhich).split("\\s+");
                Player player = Bukkit.getPlayerExact(lores[1]);
                if (player != null) {
                    if (player != event.getEntity()) {
                        event.setCancelled(true);
                    } else {
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setLore(new ArrayList<>());
                        item.setItemMeta(itemMeta);
                    }
                } else {
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setLore(new ArrayList<>());
                    item.setItemMeta(itemMeta);
                }
            } else if (isCoin) {
                 String[] lores = lore.get(slotatwhich).split("\\s+");
                 int money = Integer.parseInt(lores[1].replace("$", ""));
                 econ.depositPlayer((Player) event.getEntity(), money);
                 event.setCancelled(true);
                 event.getItem().remove();
            }
        }
    }
}
