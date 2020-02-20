package org.ragearcade;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.ragearcade.Commands.*;
import org.ragearcade.Listeners.*;
import org.ragearcade.Listeners.JDA.GuildMemberRoleAdd;
import org.ragearcade.Listeners.JDA.GuildMemberRoleRemove;
import org.ragearcade.Listeners.JDA.GuildMessageReceived;
import org.ragearcade.Listeners.JDA.PrivateMessageReceived;
import org.ragearcade.Listeners.Luckperms.NodeAdd;
import org.ragearcade.Utils.Utils;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {
    private static Connection connection;
    private static Economy econ = null;
    private static JDA jda;
    public static HashMap<UUID, String> UUIDCodeMap = new HashMap<>();
    public static HashMap<UUID, String> dcUUIDIdMap = new HashMap<>();
    public static HashMap<UUID, String> UUIDIdMap = new HashMap<>();
    public static HashMap<String, String> IDcodeMap = new HashMap<>();
    public static HashMap<String, UUID> IdUUIDMap = new HashMap<>();
    public static List<UUID> verifiedMembers = new ArrayList<>();
    public static Guild guild;
    public static String lastExecutedCommand = "";

    public void onEnable(){
        if (!setupEconomy() ) {
            getLogger().severe("["+Utils.messageErrorPrefix+"] - Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(getConfig().getString("discord.token")).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        registerCommands();
        registerListeners();
        saveDefaultConfig();
        MySQLSetup();
        preventMySQLTimeout();
        createPlayerData();
        Bukkit.getScheduler().runTaskLater(this, ()->{
            Utils.sendRageEventWebhook("Server is online!", Color.GREEN, jda);
        guild = jda.getGuilds().get(0);
        jda.getTextChannelsByName("》server-chat《", false).get(0).getManager().setTopic(Bukkit.getOnlinePlayers().size() + "/" +
                Bukkit.getServer().getMaxPlayers() + " players online | " + Bukkit.getOfflinePlayers().length + Bukkit.getOnlinePlayers().size() + " players in total | "
                + verifiedMembers + " verified members").queue();
        }, 60L);
        System.out.println("Enabling Custom Plugin");
    }
    public void onDisable() {
        if (lastExecutedCommand.equals("/restart")) {
            Utils.sendRageEventWebhook("Server is restarting! :recycle:", Color.YELLOW, jda);
            jda.getTextChannelsByName("》server-chat《", false).get(0).getManager().setTopic("Server Restarting | " +
                    Bukkit.getOfflinePlayers().length + Bukkit.getOnlinePlayers().size() + " players in total | " + Main.verifiedMembers.size() +
                    " verified members").queue();
        } else {
            Utils.sendRageEventWebhook("Server is offline!", Color.RED, jda);
            jda.getTextChannelsByName("》server-chat《", false).get(0).getManager().setTopic("Server Offline | " +
                    Bukkit.getOfflinePlayers().length + Bukkit.getOnlinePlayers().size() + " players in total | " + Main.verifiedMembers.size() +
                    " verified members").queue();
        }
    }
    private void registerCommands() {
        new ShopCommand();
        new VerifyCommand();
        new LinkCommand(jda);
        new RestartCommand(this);
        new SpawnCommand();
        new WorldTPCommand();
    }
    private void registerListeners() {
        new UserBalanceUpdate(this, econ);
        new InventoryClick(this, econ);
        new EntityPickupItem(this, econ);
        new BlockBreak(this, econ);
        new AsyncPlayerChat(this, jda);
        new PlayerJoin(this, jda);
        new PlayerQuit(this, jda);
        new GuildMessageReceived(jda);
        new GuildMemberRoleAdd(jda);
        new GuildMemberRoleRemove(jda);
        new PrivateMessageReceived(jda);
        new NodeAdd(jda);
        new EntityDeath(this);
        new BroadcastMessage(this, jda);
        new PlayerMove(this);
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private void MySQLSetup() {
        String host = getConfig().getString("MySQL.host");
        String database = getConfig().getString("MySQL.database");
        int port = getConfig().getInt("MySQL.port");
        String username = getConfig().getString("MySQL.username");
        String password = getConfig().getString("MySQL.password");

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password));
                System.out.println(Utils.messageSuccessPrefix + "MySQl Successfully Connected!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void preventMySQLTimeout() {
        try {
            getConnection().createStatement().executeUpdate("DELETE FROM RC_VerifiedMembers WHERE ID='aqsd7712q7437'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskLater(this, ()->preventMySQLTimeout(), 24000L);
    }
    private void createPlayerData() {
        boolean isTableCreated = false;
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet rs = databaseMetaData.getTables(null, null, "RC_VerifiedMembers", null);
            if (rs.next()) {
                isTableCreated = true;
            }
            String playerTable = "CREATE TABLE RC_VerifiedMembers (" +
                "UUID VARCHAR(255)," +
                    "ID VARCHAR(255)" +
                    ");";
            if (!isTableCreated) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(playerTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static Connection getConnection() {
        return connection;
    }


    private static void setConnection(Connection connection) {
        Main.connection = connection;
    }
}
