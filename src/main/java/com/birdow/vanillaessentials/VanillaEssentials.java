package com.birdow.vanillaessentials;

import com.birdow.vanillaessentials.commands.*;
import com.birdow.vanillaessentials.commands.links.*;
import com.birdow.vanillaessentials.handlers.AutoMessage;
import com.birdow.vanillaessentials.hooks.AdvancedBan;
import com.birdow.vanillaessentials.hooks.PlaceholderAPI;
import com.birdow.vanillaessentials.hooks.TAB;
import com.birdow.vanillaessentials.hooks.Vault;
import com.birdow.vanillaessentials.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.getPrefix;
import static com.birdow.vanillaessentials.utils.Messages.log;

public final class VanillaEssentials extends JavaPlugin {

    public static AutoMessage autoMessageHandler;

    private static VanillaEssentials instance;

    public static VanillaEssentials getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        loadConfigurations();
        loadHooks();
        registerEvents();
        registerAutoMessage();
        new Coin();
        new Dice();
        new Do();
        new Pm();
        new Pvp();
        new Try();
        new Ore();
        new Roll();
        new Chat();
        new Ignore();
        new Enderchest();
        new Broadcast();
        new Vanilla();
        new Links();
        new Site();
        new Rules();
        new Rp();
        new Monitoring();
        new Faq();
        new Discord();
        new Commands();
        log(getPrefix() + " VanillaEssentials by BIRDOW включён!");
    }

    @Override
    public void onDisable() {
        savePlayerData();
    }

    private void registerEvents() {
        registerEvent(new PlayerJoin(), this);
        registerEvent(new AsyncChat(), this);
        registerEvent(new PlayerQuit(), this);
        registerEvent(new BlockBreak(), this);
        registerEvent(new PlayerCommandPreprocess(), this);
        registerEvent(new EntityPickupItem(), this);
        registerEvent(new PrepareItemCraft(), this);
    }

    public static void registerEvent(Listener listener, Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void registerAutoMessage() {
        if (!configEnabled("autoMessage")) return;
        if (autoMessageHandler != null) autoMessageHandler.getRunnable().cancel();
        autoMessageHandler = new AutoMessage();
    }

    private void loadConfigurations() {
        setupDefaultConfig();
        setupLanguage();
        setupPlayerData();
    }

    private void loadHooks() {
        AdvancedBan.setup();
        PlaceholderAPI.setup();
        TAB.setup();
        Vault.setup();
    }
}
