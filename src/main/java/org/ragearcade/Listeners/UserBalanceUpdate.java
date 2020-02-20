package org.ragearcade.Listeners;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.*;
import net.ess3.api.events.UserBalanceUpdateEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.ragearcade.Main;
import org.ragearcade.Utils.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class UserBalanceUpdate implements Listener {

    private static Main plugin;
    private static Economy econ = null;

    public UserBalanceUpdate(Main main, Economy econ) {
        Bukkit.getServer().getPluginManager().registerEvents(this, main);
        this.plugin = main;
        this.econ = econ;
    }
    Map<String, Integer> moneyRankUpMap = new HashMap<>();
    String[] ranks = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    int[] moneyforRanks = new int[ranks.length];
    int[] prefixWeights = {50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600, 650, 700, 750, 800, 850, 900, 950, 1000, 1050, 1100, 1150, 1200, 1250, 1300};
    @EventHandler
    public void onBalanceUpdate(UserBalanceUpdateEvent event) {
//        System.out.println("EVEN TRIGGERED AGAIN?");
//        System.out.println(event.getNewBalance());
//        System.out.println(event.getOldBalance());
//        for (int i = 0; i<(ranks.length); i++) {
//            moneyforRanks[i] = plugin.getConfig().getInt("minecraft.prison.mines."+ranks[i]+".money");
//        }
//        for (int rank=0; rank<(ranks.length); rank++) {
//            moneyRankUpMap.put(ranks[rank], moneyforRanks[rank]);
//        }
        for (int money=0; money<(moneyRankUpMap.values().size()); money++) {
//            if (event.getNewBalance().toBigInteger().intValueExact() > moneyforRanks[money]) {
//                LuckPermsApi api = LuckPerms.getApi();
//                User user = api.getUserManager().loadUser(event.getPlayer().getUniqueId()).join();
//                Group group = api.getGroupManager().getGroup("prisonrank"+ranks[money].toLowerCase());
//                Group rankBefore;
//                if (ranks[money].equals("A")) {
//                    rankBefore = api.getGroupManager().getGroup("prisonrank"+ranks[money].toLowerCase());
//                } else {
//                    rankBefore = api.getGroupManager().getGroup("prisonrank"+ranks[money-1].toLowerCase());
//                }
//                if (!user.inheritsGroup(group)) {
//                    if (user.inheritsGroup(rankBefore)) {
//                        event.getPlayer().sendMessage(Utils.messageSuccessPrefix + "You are now in rank " + ranks[money]);
//                        Node prefixNode = api.getNodeFactory().makePrefixNode(prefixWeights[money], "&8[&f" + ranks[money] + "&8]").build();
//                        Node groupNode = api.getNodeFactory().makeGroupNode("prisonrank" + ranks[money].toLowerCase()).build();
//                        Node beforeGroupNode = api.getNodeFactory().makeGroupNode("prisonrank" + ranks[money-1].toLowerCase()).build();
//                        Node warpPermissionNode = api.getNodeFactory().newBuilder("essentials.warps." + ranks[money]).build();
//                        user.setPermission(prefixNode);
//                        user.setPermission(warpPermissionNode);
//                        user.setPermission(groupNode);
//                        user.unsetPermission(beforeGroupNode);
//                        econ.withdrawPlayer(Bukkit.getPlayerExact(event.getPlayer().getName()), moneyforRanks[money]);
//                        event.getPlayer().performCommand("warp " + ranks[money]);
//                        api.getUserManager().saveUser(user);
//                    } else {
//                        System.out.println(ranks[money]);
//                        System.out.println(money);
//                    }
//                }
//            }
        }
    }
}
