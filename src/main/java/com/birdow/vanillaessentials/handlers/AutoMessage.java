package com.birdow.vanillaessentials.handlers;

import com.birdow.vanillaessentials.VanillaEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.deserialize;

public class AutoMessage {

    private final Plugin plugin;
    private final String prefix;
    private final int timeInTicks;
    private final List<String> messages;
    private final int minPlayers;

    private BukkitRunnable runnable;
    private int sequence = 0;

    public AutoMessage() {
        this.plugin = VanillaEssentials.getInstance();
        this.prefix = getConfigString("autoMessage.settings.prefix");
        this.messages = getConfigStringList("autoMessage.settings.messages");
        this.timeInTicks = getConfigInt("autoMessage.settings.interval") * 20;
        this.minPlayers = getConfigInt("autoMessage.settings.minPlayers");

        if (getConfigBoolean("autoMessage.settings.randomize")) {
            randomRun();
        } else {
            sequenceRun();
        }
    }

    public void randomRun() {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable = this;
                if (Bukkit.getOnlinePlayers().size() >= minPlayers) {
                    String message = prefix + messages.get((int)(Math.random() * messages.size()));
                    sendAutoMessage(message);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, timeInTicks);
    }

    public void sequenceRun() {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable = this;
                if (Bukkit.getOnlinePlayers().size() >= minPlayers) {
                    if (sequence == messages.size()) sequence = 0;
                    String message = prefix + messages.get(sequence);
                    sendAutoMessage(message);
                    sequence++;
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, timeInTicks);
    }

    private void sendAutoMessage(String message) {
        for (Player recipient : Bukkit.getOnlinePlayers()) {
            if (getPlayerDataBoolean(recipient.getName() + ".isAutoMessageToggled")) continue;
            recipient.sendMessage(deserialize(recipient, message));
        }
    }

    public BukkitRunnable getRunnable() {
        return runnable;
    }
}
