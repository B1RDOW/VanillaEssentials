package com.birdow.vanillaessentials.utils;

import com.birdow.vanillaessentials.VanillaEssentials;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.Messages.debug;
import static com.birdow.vanillaessentials.utils.Messages.getPrefix;

public class ConfigManager {

    private static final VanillaEssentials instance = VanillaEssentials.getInstance();
    private static File languageFile;
    private static File playerdataFile;
    private static FileConfiguration languageConfig;
    private static FileConfiguration playerdataConfig;

    public static void setupLanguage() {
        languageFile = new File(instance.getDataFolder(), "language.yml");

        if (!languageFile.exists()) {
            languageFile.getParentFile().mkdirs();
            instance.saveResource("language.yml", false);
            debug(Level.INFO, getPrefix() + " The language.yml file has been created");
        }
        languageConfig = YamlConfiguration.loadConfiguration(languageFile);
    }
    public static FileConfiguration getLanguageConfig() {
        return languageConfig;
    }
    public static void reloadLanguageConfig() {
        languageConfig = new YamlConfiguration();
        loadConfig(languageConfig, languageFile, "language.yml");
    }
    public static String getLanguageConfigString(String path) {
        return getLanguageConfig().getString(path);
    }
    public static List<String> getLanguageConfigStringList(String path) {
        return getLanguageConfig().getStringList(path);
    }

    public static void setupPlayerData() {
        playerdataFile = new File(instance.getDataFolder(), "player-data.yml");

        if (!playerdataFile.exists()) {
            try {
                if (playerdataFile.createNewFile()) debug(Level.INFO, getPrefix() + " The player-data.yml file has been created");
            } catch (Exception e) {
                debug(Level.WARNING, getPrefix() + " Could not create the player-data.yml file");
            }
        }
        playerdataConfig = YamlConfiguration.loadConfiguration(playerdataFile);
    }
    public static FileConfiguration getPlayerData() {
        return playerdataConfig;
    }
    public static void reloadPlayerData() {
        playerdataConfig = new YamlConfiguration();
        loadConfig(playerdataConfig, playerdataFile, "player-data.yml");
    }
    public static void savePlayerData() {
        try {
            playerdataConfig.save(playerdataFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save player-data.yml", e);
        }
    }
    public static boolean getPlayerDataBoolean(String path) {
        return getPlayerData().getBoolean(path);
    }
    public static String getPlayerDataString(String path) {
        return getPlayerData().getString(path);
    }
    public static List<String> getPlayerDataStringList(String path) {
        return getPlayerData().getStringList(path);
    }
    public static int getPlayerDataInt(String path) {
        return getPlayerData().getInt(path);
    }
    public static void setPlayerData(String path, Object value) {
        getPlayerData().set(path, value);
        savePlayerData();
    }
    public static void setNullPlayerData(String path, Object value) {
        if (getPlayerData().get(path) == null) setPlayerData(path, value);
    }

    public static void reloadDefaultConfig() {
        debug(Level.INFO, getPrefix() + " The config.yml file has been reloaded");
        instance.reloadConfig();
    }
    public static void setupDefaultConfig() {
        File configFile = new File(instance.getDataFolder(), "config.yml");
        if (!configFile.exists()) debug(Level.INFO, getPrefix() + " The config.yml file has been created");
        instance.getConfig().options().copyDefaults();
        instance.saveDefaultConfig();
    }
    public static FileConfiguration getConfig() {
        return instance.getConfig();
    }
    public static boolean getConfigBoolean(String path) {
        return getConfig().getBoolean(path);
    }
    public static Boolean configEnabled(String path) {
        return getConfigBoolean(path + ".enabled");
    }
    public static String getConfigString(String path) {
        return getConfig().getString(path);
    }
    public static List<String> getConfigStringList(String path) {
        return getConfig().getStringList(path);
    }
    public static int getConfigInt(String path) {
        return getConfig().getInt(path);
    }

    private static void loadConfig(FileConfiguration config, File file, String fileName) {
        try {
            config.load(file);
            debug(Level.INFO, getPrefix() + " The " + fileName + " file has been reloaded");
        } catch (IOException | InvalidConfigurationException e) {
            debug(Level.WARNING, getPrefix() + " Could not reload the " + fileName + " file");
        }
    }
}
