package com.blockparty;

import wueffi.MiniGameCore.utils.Lobby;
import wueffi.MiniGameCore.api.GameStartEvent;
import wueffi.MiniGameCore.api.MiniGameCoreAPI;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.entity.PlayerDeathEvent;


import net.kyori.adventure.text.Component;

import java.util.Random;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

public class Round implements Listener {
    
    public Lobby lobby;
    public World world;
    public Player owner;
    public int PlayersAlive;

    private final BlockParty plugin;

    public Round(BlockParty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {

        if (!event.getGameName().equalsIgnoreCase("BlockParty")) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Round(1.234f);
        }, 20L * 10);

        lobby = event.getLobby();
        world = Bukkit.getWorld(lobby.getWorldFolder().getName());
        owner = lobby.getOwner();
        PlayersAlive = lobby.getPlayers().size();
    }
    
    public void Round(float SpeedMult) {

        Bukkit.dispatchCommand(
            owner,
            "clone 14 -40 15 -18 -40 -16 -18 -31 -16"
        );  
        Random random = new Random();
        int NumberColor = random.nextInt(16) + 1;
        
        final Material Color = switch (NumberColor) {
            case 1 -> Material.RED_WOOL;
            case 2 -> Material.ORANGE_WOOL;
            case 3 -> Material.YELLOW_WOOL;
            case 4 -> Material.LIME_WOOL;
            case 5 -> Material.GREEN_WOOL;
            case 6 -> Material.CYAN_WOOL;
            case 7 -> Material.LIGHT_BLUE_WOOL;
            case 8 -> Material.BLUE_WOOL;
            case 9 -> Material.PURPLE_WOOL;
            case 10 -> Material.MAGENTA_WOOL;
            case 11 -> Material.PINK_WOOL;
            case 12 -> Material.WHITE_WOOL;
            case 13 -> Material.LIGHT_GRAY_WOOL;
            case 14 -> Material.GRAY_WOOL;
            case 15 -> Material.BLACK_WOOL;
            case 16 -> Material.BROWN_WOOL;
            default -> Material.BLACK_WOOL;
        };

        ItemStack item = new ItemStack(Color, 1);
        for (Player player : lobby.getPlayers()) {
            player.getInventory().setItem(1, item);
            player.getInventory().setItem(7, item);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            
            for (Player player : lobby.getPlayers()) {
                player.getInventory().setItem(1, item);
                player.getInventory().setItem(7, item);
            }
        }, (long) (20L * 0.1));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : lobby.getPlayers()) {
                player.sendActionBar("§c3");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0f, 1.0f);
            }
        }, (long) (20L * 1 * SpeedMult));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : lobby.getPlayers()) {
                player.sendActionBar("§c2");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0f, 1.0f);
            }
        }, (long) (20L * 2 * SpeedMult));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : lobby.getPlayers()) {
                player.sendActionBar("§c1");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0f, 1.0f);
            }
        }, (long) (20L * 3 * SpeedMult));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : lobby.getPlayers()) {
                player.sendActionBar("§cWatch Your Step!");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0f, 2.0f);
            }
            RemoveAllBlocksExcept(Color);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Bukkit.dispatchCommand(
                    owner,
                    "clone 14 -40 15 -18 -40 -16 -18 -31 -16"
                );
            }, 20L * 3);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (lobby.getPlayers().size() == 1) {
                    Player winner = lobby.getPlayers().iterator().next();

                    Bukkit.broadcastMessage("§a" + winner.getName() + " has won!");

                }
                Round(SpeedMult * 0.95f);
            }, 20L * 5);

        }, (long) (20L * 4 * SpeedMult));
    }

    public void RemoveAllBlocksExcept(Material blockType) {
        for (int x = -17; x <= 14; x++) {
            for (int z = -16; z <= 15; z++) {
                Block block = world.getBlockAt(x, -31, z);
                if (block.getType() != blockType) {
                    block.setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!lobby.getPlayers().contains(player)) {
            return;
        }

        PlayersAlive -= 1;

        if (PlayersAlive == 1) {
            Player winner = lobby.getPlayers().iterator().next();
            MiniGameCoreAPI.winPlayer(lobby, winner);
        }
    }
}