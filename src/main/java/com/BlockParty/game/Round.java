package com.BlockParty.game;

import com.BlockParty.BlockParty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NonNull;
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

import java.util.Random;

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

        Bukkit.getScheduler().runTaskLater(plugin, () -> Round(1.234f), 20L * 10);

        lobby = event.getLobby();
        world = Bukkit.getWorld(lobby.getWorldFolder().getName());
        owner = lobby.getOwner();
        PlayersAlive = lobby.getPlayers().size();
    }
    
    public void Round(float SpeedMult) {
        Bukkit.getScheduler().runTaskLater(plugin, this::RestoreArena, 20L * 3);
        final Material Color = getColor();

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
                player.sendActionBar(Component.text("3", NamedTextColor.RED));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0f, 1.0f);
            }
        }, (long) (20L * 1 * SpeedMult));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : lobby.getPlayers()) {
                player.sendActionBar(Component.text("2", NamedTextColor.RED));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0f, 1.0f);
            }
        }, (long) (20L * 2 * SpeedMult));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : lobby.getPlayers()) {
                player.sendActionBar(Component.text("1", NamedTextColor.RED));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0f, 1.0f);
            }
        }, (long) (20L * 3 * SpeedMult));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : lobby.getPlayers()) {
                player.sendActionBar(Component.text("Watch Your Step!", NamedTextColor.RED));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0f, 2.0f);
            }
            RemoveAllBlocksExcept(Color);
            Bukkit.getScheduler().runTaskLater(plugin, this::RestoreArena, 20L * 3);
            Bukkit.getScheduler().runTaskLater(plugin, () -> Round(SpeedMult * 0.95f), 20L * 5);

        }, (long) (20L * 4 * SpeedMult));
    }

    private static @NonNull Material getColor() {
        Random random = new Random();
        int NumberColor = random.nextInt(16) + 1;

        return switch (NumberColor) {
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
            case 15 -> Material.BROWN_WOOL;
            default -> Material.BLACK_WOOL;
        };
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

    public void RestoreArena() {
        for (int x = -18; x <= 14; x++) {
            for (int z = -16; z <= 15; z++) {
                Block source = world.getBlockAt(x, -40, z);
                Block target = world.getBlockAt(x, -31, z);

                target.setBlockData(source.getBlockData(), false);
            }
        }
    }
}