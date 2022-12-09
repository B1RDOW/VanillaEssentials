package com.birdow.vanillaessentials.hooks;

import com.birdow.vanillaessentials.VanillaEssentials;
import com.birdow.vanillaessentials.expansions.VanillaExpansion;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigBoolean;
import static com.birdow.vanillaessentials.utils.Messages.getPrefix;
import static com.birdow.vanillaessentials.utils.Messages.debug;
import static org.bukkit.Bukkit.getServer;

public class PlaceholderAPI {

    public static void setup() {
        if (!getConfigBoolean("Hooks.PlaceholderAPI")) {
            debug(Level.INFO, getPrefix() + " PlaceholderAPI hook disabled in config");
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            (new VanillaExpansion()).register();
            debug(Level.INFO, getPrefix() + " PlaceholderAPI successfully hooked");
        } else {
            debug(Level.WARNING, getPrefix() + " PlaceholderAPI didn't hook!");
            getServer().getPluginManager().disablePlugin(VanillaEssentials.getInstance());
        }
    }
}
