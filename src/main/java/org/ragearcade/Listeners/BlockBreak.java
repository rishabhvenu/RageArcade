package org.ragearcade.Listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.util.Random;

public class BlockBreak implements Listener {

    private static Economy econ;

    public BlockBreak(Main main, Economy economy) {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
        this.econ = economy;
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location brokenBlockLoc = block.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(brokenBlockLoc));
        String isInMine = "outside";
        for (ProtectedRegion region: set) {
            if (region.getId().startsWith("warp")) {
                isInMine = "warp";
            }
            if (region.getId().startsWith("mine")) {
                isInMine = "mine";
                break;
            }
        }
        if (isInMine.equals("mine")) {
            for (int i = 0; i < (Utils.itemBlocks.length); i++) {
                if (block.getType() == Utils.itemBlocks[i]) {
                    ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
                    int money = Integer.parseInt(Utils.oreCosts[i].trim().split("\\s+")[1].replace("$", ""));
                    int amount = 1;
                    if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                        if (event.getBlock().getType().equals(Material.LAPIS_ORE)) {
                            amount = (new Random().nextInt(5 - 9) + 5) * new Random().nextInt(1 - item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS))
                                    + 1;
                        } else {
                            amount = new Random().nextInt(1 - item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)) + 1;
                        }
                    }
                    econ.depositPlayer(event.getPlayer(), amount * money);
                    event.setCancelled(true);
                }
            }
        }
    }
}
