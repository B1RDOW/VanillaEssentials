package com.birdow.vanillaessentials.listeners;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setNullPlayerData(player.getName() + ".joined", false);
        if (!getPlayerDataBoolean(player.getName() + ".joined")) {
            setPlayerData(player.getName() + ".joined", true);
            onFirstJoin(player);
        }
        if (configEnabled("customJoinMessage")) {
            event.joinMessage(null);
            if (getPlayerDataBoolean(player.getName() + ".isVanished")) return;
            for (Player recipient : Bukkit.getOnlinePlayers()) {
                if (!getPlayerDataBoolean(recipient.getName() + ".isJoinMessageToggled")) {
                    sendPlayerMessage(player, recipient, message("playerJoinMessage"));
                }
            }
            log("<player> Присоеденился к игре!".replace("<player>", player.getName()));
        }
    }

    private void onFirstJoin(Player player) {
        skinLoad(player);

        setNullPlayerData(player.getName() + ".theme", "default");
        if (configEnabled("chat")) {
            if (configEnabled("pm") && getConfigBoolean("pm.soundNotify") && getPlayerData().get(player.getName() + ".pmSoundNotify") == null) {
                setPlayerData(player.getName() + ".pmSoundNotify", true);
            }
            if (getPlayerData().get(player.getName() + ".isGlobalChatToggled") == null) {
                setPlayerData(player.getName() + ".isGlobalChatToggled", false);
            } else if (getPlayerDataBoolean(player.getName() + ".isChatToggled")) {
                player.sendMessage(deserialize(player, message("chatMessagesDisable")));
            }
        }
        if (configEnabled("pvp")) setNullPlayerData(player.getName() + ".pvp", getConfigBoolean("pvp.defaultSetting"));
        if (configEnabled("autoMessage")) setNullPlayerData(player.getName() + ".isAutoMessageToggled", false);
        if (configEnabled("deathMessage")) setNullPlayerData(player.getName() + ".isDeathMessageToggled", false);
        if (configEnabled("customJoinMessage") || configEnabled("customQuitMessage")) setNullPlayerData(player.getName() + ".isJoinMessageToggled", false);
        if (configEnabled("reputation")) setNullPlayerData(player.getName() + ".reputation", getConfigInt("reputation.settings.startReputation"));
        if (configEnabled("tags") && getConfigBoolean("Hooks.TAB")) {
            setNullPlayerData(player.getName() + ".isTagsTitle", true);
            setNullPlayerData(player.getName() + ".isTagsToggled", false);
            if (!getPlayerDataBoolean(player.getName() + ".isTagsToggled")) {
                tagToggle(player);
            }
        }
        if (configEnabled("oreMoney")) {
            setNullPlayerData(player.getName() + ".oreAutoSell", false);
            setNullPlayerData(player.getName() + ".orePickupSell", false);
        }
    }

    public void skinLoad(Player player) {
        try {
            Thread.sleep(2000);
            Bukkit.dispatchCommand(player, "skin set " + player.getName().toLowerCase());
        } catch (InterruptedException e) {
            debug(Level.WARNING, getPrefix() + " SkinLoad error (PlayerJoin.java)!\n" + e);
        }
    }

    private void tagToggle(Player player) {
        try {
            Thread.sleep(2000);
            TeamManager teams = TabAPI.getInstance().getTeamManager();
            teams.toggleNameTagVisibilityView(TabAPI.getInstance().getPlayer(player.getUniqueId()), false);
        } catch (InterruptedException e) {
            debug(Level.WARNING, getPrefix() + " TagToggle error (PlayerJoin.java)!\n" + e);
        }
    }
}