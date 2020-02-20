package org.ragearcade.Listeners;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryClick implements Listener {
    private static Economy econ = null;
    public InventoryClick(Main main, Economy econ) {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
        this.econ = econ;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            String invName = event.getView().getTitle();
            if (invName.equals(Utils.chat("&5Item Shop"))) {
                event.setCancelled(true);
                Material currentItemType = event.getCurrentItem().getType();
                if (currentItemType.equals(Material.DIAMOND)) {
                    Utils.openOreShopGUI(player);
                } else if (currentItemType.equals(Material.IRON_PICKAXE)) {
                    Utils.openToolShopGUI(player);
                }
            } else if (invName.equals(Utils.chat("&5Ore Shop"))) {
                event.setCancelled(true);
                Material currentItemType = event.getCurrentItem().getType();
                if (currentItemType.equals(Material.ARROW)) {
                    Utils.openShopGUI(player);
                }
                for (int i=0;i<(Utils.items.length);i++) {
                    if (currentItemType.equals(Utils.items[i])) {
                        if (event.getClick() == ClickType.LEFT) {
                            Utils.openConfirmBuyOreItem(Utils.items[i],  Utils.oreDisplayNames[i], player, Utils.oreCosts[i], true);
                        } else if (event.getClick() == ClickType.RIGHT) {
                            Utils.openConfirmBuyOreItem(Utils.items[i], Utils.oreDisplayNames[i], player, Utils.oreLoreSell[i], false);
                        }
                    }
                }
            } else if (invName.equals(Utils.chat("&5Tool Shop"))) {
                event.setCancelled(true);
                Material currentItemType = event.getCurrentItem().getType();
                if (currentItemType.equals(Material.ARROW)) {
                    Utils.openShopGUI(player);
                }
                for (int i=0;i<(Utils.tools.length);i++) {
                    if (currentItemType.equals(Utils.tools[i])) {
                        if (event.getClick() == ClickType.LEFT) {
                            ItemStack slotItem = new ItemStack(Utils.tools[i]);
                            String[] splitLore = Utils.toolCosts[i].trim().split("\\s+");
                            int cost;
                            cost = Integer.parseInt(splitLore[1].replace("$", ""));
                            int finalCost = cost * slotItem.getAmount();
                            EconomyResponse ec = econ.withdrawPlayer(player, finalCost);
                            if (ec.transactionSuccess()) {
                                HashMap<Integer, ItemStack> leftOver = new HashMap<>();
                                leftOver.putAll(player.getInventory().addItem(new ItemStack(slotItem.getType(), slotItem.getAmount())));
                                if (!leftOver.isEmpty()) {
                                    Location loc = player.getLocation();
                                    ItemStack droppedItem;
                                    droppedItem = new ItemStack(leftOver.get(0).getType(), leftOver.get(0).getAmount());
                                    ItemMeta droppedItemMeta = droppedItem.getItemMeta();
                                    List<String> droppedItemLore = new ArrayList<>();
                                    droppedItemLore.add(Utils.chat("&5Player: " + player.getName()));
                                    droppedItemMeta.setLore(droppedItemLore);
                                    droppedItem.setItemMeta(droppedItemMeta);
                                    player.getWorld().dropItem(loc, droppedItem);
                                }
                                player.sendMessage(Utils.messageSuccessPrefix + Utils.chat("You successfully purchased " + slotItem.getAmount() + " " + slotItem.getType()));
                                } else {
                                    player.sendMessage(Utils.messageErrorPrefix + Utils.chat("You don't have sufficient funds!"));
                                }
                        } else if (event.getClick() == ClickType.RIGHT) {
                            ItemStack slotItem = new ItemStack(Utils.tools[i]);
                            String[] splitLore = Utils.toolLoreSell[i].trim().split("\\s+");
                            int cost;
                            cost = Integer.parseInt(splitLore[2].replace("$", ""));
                            int finalCost = cost * slotItem.getAmount();
                            EconomyResponse ec = econ.depositPlayer(player, finalCost);
                            if (ec.transactionSuccess()) {
                                if (player.getInventory().containsAtLeast(new ItemStack(slotItem.getType()), slotItem.getAmount())) {
                                    player.getInventory().removeItem(new ItemStack(slotItem.getType(), slotItem.getAmount()));
                                    player.sendMessage(Utils.messageSuccessPrefix + Utils.chat("You successfully sold " + slotItem.getAmount() + " " + slotItem.getType()));
                                } else {
                                    player.sendMessage(Utils.messageErrorPrefix + Utils.chat("You don't have the required items!"));
                                }
                            } else {
                                player.sendMessage(Utils.messageErrorPrefix + Utils.chat("You have already reached the max amount of money!"));
                            }
                        }
                    }
                }
            } else if (invName.equals(Utils.chat("&5Confirm Buy")) || invName.equals(Utils.chat("&5Confirm Sell"))) {
                event.setCancelled(true);
                String whichInv = null;
                for (int i = 0; i<(Utils.items.length);i++){
                    if (event.getClickedInventory().getItem(22).getType() == Utils.items[i]) {
                        whichInv = "Ore";
                        break;
                    }
                }
                for (int i = 0; i<(Utils.tools.length);i++) {
                    if (event.getClickedInventory().getItem(22).getType() == Utils.tools[i]) {
                        whichInv = "Tool";
                        break;
                    }
                }
                if (whichInv.equals("Ore")) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        Material currentItemType = event.getCurrentItem().getType();
                        String currentDisplayName = event.getCurrentItem().getItemMeta().getDisplayName();
                        Inventory clickedInv = event.getClickedInventory();
                        ItemStack is = clickedInv.getItem(22);
                        if (currentDisplayName.equals(Utils.chat("&aAdd 1"))) {
                            if (is.getAmount() < 64) {
                                is.setAmount(is.getAmount() + 1);
                            } else {
                                is.setAmount(64);
                            }
                            clickedInv.setItem(22, is);
                        } else if (currentDisplayName.equals(Utils.chat("&aAdd 10"))) {
                            if (is.getAmount() <= 54) {
                                is.setAmount(is.getAmount() + 10);
                            } else {
                                is.setAmount(64);
                            }
                            clickedInv.setItem(22, is);
                        } else if (currentDisplayName.equals(Utils.chat("&aAdd 64"))) {
                            is.setAmount(64);
                            clickedInv.setItem(22, is);
                        } else if (currentDisplayName.equals(Utils.chat("&cRemove 1"))) {
                            if (is.getAmount() > 1) {
                                is.setAmount(is.getAmount() - 1);
                            } else {
                                is.setAmount(1);
                            }
                            clickedInv.setItem(22, is);
                        } else if (currentDisplayName.equals(Utils.chat("&cRemove 10"))) {
                            if (is.getAmount() >= 11) {
                                is.setAmount(is.getAmount() - 10);
                            } else {
                                is.setAmount(1);
                            }
                            clickedInv.setItem(22, is);
                        } else if (currentDisplayName.equals(Utils.chat("&cRemove 64"))) {
                            is.setAmount(1);
                            clickedInv.setItem(22, is);
                        }
                        if (currentItemType.equals(Material.ARROW) || currentItemType.equals(Material.BARRIER)) {
                            Utils.openOreShopGUI(player);
                        } else if (currentItemType.equals(Material.GREEN_TERRACOTTA) || currentItemType.equals(Material.RED_TERRACOTTA)) {
                            ItemStack slotItem = event.getClickedInventory().getItem(22);
                            List<String> lore = slotItem.getItemMeta().getLore();
                            String[] splitLore = lore.get(0).trim().split("\\s+");
                            int cost;
                            if (invName.equals(Utils.chat("&5Confirm Buy"))) {
                                cost = Integer.parseInt(splitLore[1].replace("$", ""));
                            } else {
                                cost = Integer.parseInt(splitLore[2].replace("$", ""));
                            }
                            int finalCost = cost * slotItem.getAmount();
                            if (invName.equals(Utils.chat("&5Confirm Buy"))) {
                                EconomyResponse ec = econ.withdrawPlayer(player, finalCost);
                                if (ec.transactionSuccess()) {
                                    HashMap<Integer, ItemStack> leftOver = new HashMap<>();
                                    leftOver.putAll(player.getInventory().addItem(new ItemStack(slotItem.getType(), slotItem.getAmount())));
                                    if (!leftOver.isEmpty()) {
                                        Location loc = player.getLocation();
                                        ItemStack droppedItem;
                                        droppedItem = new ItemStack(leftOver.get(0).getType(), leftOver.get(0).getAmount());
                                        ItemMeta droppedItemMeta = droppedItem.getItemMeta();
                                        List<String> droppedItemLore = new ArrayList<>();
                                        droppedItemLore.add(Utils.chat("&5Player: " + player.getName()));
                                        droppedItemMeta.setLore(droppedItemLore);
                                        droppedItem.setItemMeta(droppedItemMeta);
                                        player.getWorld().dropItem(loc, droppedItem);
                                    }
                                    player.sendMessage(Utils.messageSuccessPrefix + Utils.chat("You successfully purchased " + slotItem.getAmount() + " " + slotItem.getType()));
                                } else {
                                    player.sendMessage(Utils.messageErrorPrefix + Utils.chat("You don't have sufficient funds!"));
                                }
                            } else {
                                EconomyResponse ec = econ.depositPlayer(player, finalCost);
                                double balance = econ.getBalance(player);
                                if (ec.transactionSuccess()) {
                                    if (player.getInventory().containsAtLeast(new ItemStack(slotItem.getType()), slotItem.getAmount())) {
                                        player.getInventory().removeItem(new ItemStack(slotItem.getType(), slotItem.getAmount()));
                                        player.sendMessage(Utils.messageSuccessPrefix + Utils.chat("You successfully sold " + slotItem.getAmount() + " " + slotItem.getType()));
                                    } else {

                                        player.sendMessage(Utils.messageErrorPrefix + Utils.chat("You don't have the required items!"));
                                    }
                                } else {
                                    if (econ.getBalance(player) != balance) {
                                        econ.withdrawPlayer(player, finalCost);
                                    }
                                    player.sendMessage(Utils.messageErrorPrefix + Utils.chat("You have already reached the max amount of money!"));
                                }
                            }
                            player.closeInventory();
                        }
                    }
                }
            } else {
                return;
            }
        } else {
            return;
        }
    }
}
