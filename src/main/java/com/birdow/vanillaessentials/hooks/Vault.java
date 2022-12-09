package com.birdow.vanillaessentials.hooks;


import com.birdow.vanillaessentials.VanillaEssentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigBoolean;
import static com.birdow.vanillaessentials.utils.Messages.getPrefix;
import static com.birdow.vanillaessentials.utils.Messages.debug;
import static org.bukkit.Bukkit.getServer;

public class Vault {

    private static Economy econ = null;
    public static Economy getEconomy() {
        return econ;
    }

    public static void setup() {
        if (!getConfigBoolean("Hooks.Vault")) {
            debug(Level.INFO, getPrefix() + " VaultAPI hook disabled in config");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (getServer().getPluginManager().getPlugin("Vault") == null || rsp == null) {
            debug(Level.WARNING, getPrefix() + " VaultAPI didn't hook!");
            getServer().getPluginManager().disablePlugin(VanillaEssentials.getInstance());
        } else {
            econ = rsp.getProvider();
            debug(Level.INFO, getPrefix() + " VaultAPI successfully hooked");
        }
    }
}
