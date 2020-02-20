package org.ragearcade.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityDeath implements Listener {

    public EntityDeath(Main main) {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Location loc = event.getEntity().getLocation();
        loc.getWorld().playEffect(event.getEntity().getLocation().add(0,1,0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        if (!(event.getEntity() instanceof Player)) {
            loc.getWorld().playEffect(event.getEntity().getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.GOLD_BLOCK);
        }
        Location dropLocation = event.getEntity().getLocation();
        ItemStack coin = new ItemStack(Material.SUNFLOWER);
        ItemMeta coinMeta = coin.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        if (event.getEntity().getCustomName() != null) {
            if (event.getEntityType() == EntityType.ZOMBIE) {
                if (event.getEntity().getCustomName().equals(Utils.chat("&7Zombie"))) {
                    itemLore.add(Utils.chat("&5Coin: $1500"));
                    coinMeta.setLore(itemLore);
                    coin.setItemMeta(coinMeta);
                    event.getEntity().getWorld().dropItem(dropLocation, coin);
                }
            }
        } else {
            if (event.getEntity() != event.getEntity().getKiller()) {
                itemLore.add(Utils.chat("&5Coin: $200"));
                coinMeta.setLore(itemLore);
                coin.setItemMeta(coinMeta);
                event.getEntity().getWorld().dropItem(dropLocation, coin);
            }
        }
    }
}
