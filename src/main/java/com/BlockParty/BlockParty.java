package com.blockparty;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import wueffi.MiniGameCore.managers.LobbyManager;
import wueffi.MiniGameCore.utils.Lobby;
import org.bukkit.World;

public final class BlockParty extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("BlockParty has been enabled!");
        getLogger().info("GameManager initialized!");

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(
            new Round(this),
            this
        ); 


        Bukkit.getScheduler().runTaskLater(this, () -> {
            Bukkit.broadcastMessage("§aBlockParty is ready! v1.0");
        }, 20L * 10);
    }

    @Override
    public void onDisable() {
        getLogger().info("BlockParty has been disabled!");
    }
    

}