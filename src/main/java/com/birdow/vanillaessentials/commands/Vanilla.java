package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.VanillaEssentials;
import com.birdow.vanillaessentials.handlers.CustomCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class Vanilla extends CustomCommand {

    public Vanilla() {
        super("vanilla", true, "vanilla");
    }

    private static final VanillaEssentials instance = VanillaEssentials.getInstance();

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                reloadDefaultConfig();
                reloadLanguageConfig();
                reloadPlayerData();
                instance.registerAutoMessage();
                sender.sendMessage(deserialize(sender, message("configReloaded")));
            }
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(deserialize(sender, stringListToLines(getLanguageConfigStringList("messages.vanillaHelp"))));
            }
            if (args[0].equalsIgnoreCase("restart")) {
                if (args.length > 1 && args[1].equalsIgnoreCase("confirm")) {
                    onRestart();
                    return;
                }
                sender.sendMessage(deserialize(sender, message("restartConfirmation")));
                sendHelpMessage(sender, message("confirmationButton"), "/vanilla restart confirm");
            }
            if (args[0].equalsIgnoreCase("stop")) {
                if (args.length > 1 && args[1].equalsIgnoreCase("confirm")) {
                    onStop();
                    return;
                }
                sender.sendMessage(deserialize(sender, message("stopConfirmation")));
                sendHelpMessage(sender, message("confirmationButton"), "/vanilla stop confirm");
            }
        } else {
            sendHelpMessage(sender, message("vanillaCommandUsage"), "/vanilla ");
        }
    }

    private void onStop() {
        List<String> randomColors = getRandomColor();
        String prefix = message("stopPrefix"), stopGetReady = message("stopGetReady"), stopStarted = message("stopStarted"), stopKickMessage = message("stopKickMessage");
        String randomAccent = randomColors.get(0), randomPrimary = randomColors.get(1);
        sendBroadcastMessages(randomAccent + prefix + randomPrimary + stopGetReady);
        new BukkitRunnable() {
            @Override
            public void run() {
                sendBroadcastMessages(randomAccent + prefix + randomPrimary + stopStarted);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player recipient : Bukkit.getOnlinePlayers()) {
                            recipient.kick(deserialize(recipient, stopKickMessage));
                        }
                        Bukkit.shutdown();
                    }
                }.runTaskLater(instance, 20 * 10);
            }
        }.runTaskLater(instance, 20 * 30);
    }

    private void onRestart() {
        List<String> randomColors = getRandomColor();
        String prefix = message("restartPrefix"), restartGetReady = message("restartGetReady"), restartFor = message("restartFor"), restartStarted = message("restartStarted"), restartKickMessage = message("restartKickMessage");
        String randomAccent = randomColors.get(0), randomPrimary = randomColors.get(1);
        Player randomPlayer = (Player) Bukkit.getOnlinePlayers().toArray()[new Random().nextInt(Bukkit.getOnlinePlayers().size())];
        sendBroadcastMessages(randomAccent + prefix + randomPrimary + restartGetReady);
        Bukkit.broadcast(miniMessage().deserialize(randomAccent + prefix + randomPrimary + restartFor.replace("<player>", randomAccent + randomPlayer.getName())));
        new BukkitRunnable() {
            @Override
            public void run() {
                sendBroadcastMessages(randomAccent + prefix + randomPrimary + restartStarted);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player recipient : Bukkit.getOnlinePlayers()) {
                            recipient.kick(deserialize(recipient, restartKickMessage));
                        }
                        Bukkit.reload();
                    }
                }.runTaskLater(instance, 20 * 10);
            }
        }.runTaskLater(instance, 20 * 30);
    }

    private void sendBroadcastMessages(String message) {
        try {
            for (int i = 0; i < 5; i++) {
                Bukkit.broadcast(miniMessage().deserialize(message));
                TimeUnit.MILLISECONDS.sleep(200);
            }
        } catch (InterruptedException e) {
            debug(Level.WARNING, getPrefix() + " SendBroadcastMessages error (Vanilla.java)!\n" + e);
        }
    }

    private List<String> getRandomColor() {
        List<String> themes = new ArrayList<>(Objects.requireNonNull(getConfig().getConfigurationSection("themes")).getKeys(false));
        String randomGroup = themes.get(new Random().nextInt(themes.size()));
        List<String> colors = new ArrayList<>();
        colors.add("<" + getConfigString("themes." + randomGroup + "." + "accentColor") + ">");
        colors.add("<" + getConfigString("themes." + randomGroup + "." + "primaryColor") + ">");
        return colors;
    }

    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("reload", "restart", "stop", "help");
        return Lists.newArrayList();
    }
}