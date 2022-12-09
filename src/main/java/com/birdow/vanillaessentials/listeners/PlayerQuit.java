package com.birdow.vanillaessentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class PlayerQuit implements Listener {

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (configEnabled("customQuitMessage")) {
            event.quitMessage(null);
            if (getPlayerDataBoolean(player.getName() + ".isVanished")) return;
            for (Player recipient : Bukkit.getOnlinePlayers()) {
                if (!getPlayerDataBoolean(recipient.getName() + ".isJoinMessageToggled")) {
                    sendPlayerMessage(player, recipient, message("playerQuitMessage"));
                }
            }
            log("<player> Вышел из игры!".replace("<player>", player.getName()));
        }
    }
}
