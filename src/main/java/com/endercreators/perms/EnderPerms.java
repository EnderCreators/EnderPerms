package com.endercreators.perms;


import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnderPerms extends JavaPlugin {

    private MySQLManager mysqlmanager;
    private RunThread thread = null;

    public HashMap<String, List<String>> cachegroup = new HashMap<String, List<String>>();

    public static Permission perms = null;
    public static Chat chat = null;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        if (!setupPermissions()) {
            getLogger().info("Stopping plugin. Vault not found.");
            return;
        }


        thread = new RunThread("EnderPerms");
        thread.start();

        go(new Runnable() {
            @Override
            public void run() {
                getLogger().info("test");
            }
        });
        getLogger().info("go");
        if (thread == null)
            getLogger().info("nope " + thread.getName());


        mysqlmanager = new MySQLManager(this);
        if (getConfig().getLong("refresh") < 60)
            getLogger().info("Warning: Refresh time is under 60 secs.");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                refresh();
            }
        }, 60, getConfig().getLong("refresh") * 20);

    }

    public void go(Runnable runnable) {
        thread.run(runnable);
    }

    public MySQLManager getMySQLManager() {
        return mysqlmanager;
    }

    public EnderPerms getPlugin() {
        return this;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }


    @Override
    public void onDisable() {
        thread.stop();
    }

    public void refresh() {
        go(new Runnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    removeFromAllGroups(player);
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    removeFromAllGroups(player);
                    String special = getMySQLManager().getGroup(player.getName());
                    if (special != null)
                        addToGroup(player, special);
                    addToGroup(player, getConfig().getString("default"));

                }
            }
        });

    }

    public void addToGroup(Player player, String group) {
        perms.playerAddGroup("NULL", player.getName(), group);
        List<String> groups = cachegroup.get(player.getName());
        if (groups == null)
            groups = new ArrayList<String>();
        groups.add(group);
        cachegroup.remove(player.getName());
        cachegroup.put(player.getName(), groups);
    }

    public void removeFromGroup(Player player, String group) {
        perms.playerRemoveGroup("NULL", player.getName(), group);
        List<String> groups = cachegroup.get(player.getName());
        if (groups == null)
            groups = new ArrayList<String>();
        groups.remove(group);
        cachegroup.remove(player.getName());
        cachegroup.put(player.getName(), groups);
    }

    public void removeFromAllGroups(Player player) {
        List<String> groups = cachegroup.get(player.getName());
        if (groups == null)
            groups = new ArrayList<String>();
        for (String group : groups) {
            player.sendMessage("G: " + group);
            removeFromGroup(player, group);

        }
        cachegroup.remove(player.getName());
    }

    public List<String> getGroups() {
        return getConfig().getStringList("groups");
    }
}
