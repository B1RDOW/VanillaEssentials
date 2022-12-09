package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.hooks.Vault;
import com.birdow.vanillaessentials.handlers.CustomCommand;
import com.google.common.collect.Lists;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Ore extends CustomCommand {

    public Ore() {
        super("ore", "oreMoney");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (!getConfigBoolean("Hooks.Vault")) {
            player.sendMessage(deserialize(player, message("disabledCommand")));
            debug(Level.INFO, getPrefix() + " VaultAPI hook disabled in config");
            return;
        }
        Economy economy = Vault.getEconomy();
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal") || args[0].equalsIgnoreCase("money")) {
                player.sendMessage(deserialize(player, message("balance").replace("<count>", integerToStack((int) economy.getBalance(player)))));
            }
            if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage(deserialize(player, stringListToLines(getLanguageConfigStringList("messages.oreHelp"))));
            }
            if (args[0].equalsIgnoreCase("autosell")) {
                setPlayerData(player.getName() + ".oreAutoSell", !getPlayerDataBoolean(player.getName() + ".oreAutoSell"));
                if (getPlayerDataBoolean(player.getName() + ".oreAutoSell")) {
                    player.sendMessage(deserialize(player, message("autosellEnable")));
                } else {
                    player.sendMessage(deserialize(player, message("autosellDisable")));
                }
            }
            if (args[0].equalsIgnoreCase("pickupsell")) {
                setPlayerData(player.getName() + ".orePickupSell", !getPlayerDataBoolean(player.getName() + ".orePickupSell"));
                if (getPlayerDataBoolean(player.getName() + ".orePickupSell")) {
                    player.sendMessage(deserialize(player, message("pickupsellEnable")));
                } else {
                    player.sendMessage(deserialize(player, message("pickupsellDisable")));
                }

            }
            if (args[0].equalsIgnoreCase("sell")) {
                if (args.length < 2) {
                    sendHelpMessage(player, message("oreCommandUsage"), "/ore sell ");
                    return;
                }
                List<Material> oreMaterials = new ArrayList<>(stringListToMaterialList(getConfigStringList("oreMoney.settings.sellItem")));
                HashMap<Material, Integer> oreCounts = new HashMap<>();
                ItemStack[] inventory = player.getInventory().getContents();
                ItemStack leftHand = player.getInventory().getItemInOffHand();
                ItemStack helmet = player.getInventory().getHelmet();
                int requested;
                int allOres = 0;
                for (Material material : oreMaterials) {
                    int itemCount = 0;
                    for (ItemStack item : inventory) {
                        if (item != null) {
                            if (item.getType() == material) itemCount += item.getAmount();
                        }
                    }
                    if (leftHand.getType() == material) itemCount -= leftHand.getAmount();
                    if (helmet != null) {
                        if (helmet.getType() == material) itemCount -= helmet.getAmount();
                    }
                    oreCounts.put(material, itemCount);
                    allOres += itemCount;
                }
                try {
                    if (args[1].equalsIgnoreCase("all")) requested = allOres;
                    else requested = stackToInteger(args[1]);
                    if (requested <= 0) throw new Exception();
                } catch (Exception e) {
                    player.sendMessage(deserialize(player, message("invalidPrice")));
                    return;
                }
                int requestedTemp = allOres - (allOres - requested);
                if (requested <= allOres) {
                    for (Material material : oreMaterials) {
                        if (requestedTemp <= oreCounts.get(material)) {
                            player.getInventory().removeItem(new ItemStack(material, requestedTemp));
                            break;
                        }
                        player.getInventory().removeItem(new ItemStack(material, oreCounts.get(material)));
                        requestedTemp -= oreCounts.get(material);
                    }
                    economy.depositPlayer(player, requested * getConfigInt("oreMoney.settings.oreMultiplier"));
                    player.sendMessage(deserialize(player, message("sellSuccess").replace("<count>", integerToStack(requested))));
                } else {
                    player.sendMessage(deserialize(player, message("lackOfOre").replace("<count>", integerToStack(requested - allOres))));
                }
            }
            if (args[0].equalsIgnoreCase("get")) {
                if (args.length < 3) {
                    sendHelpMessage(player, message("oreCommandUsage"), "/ore get ");
                    return;
                }
                List<String> oreNames = new ArrayList<>(getConfigStringList("oreMoney.settings.sellItem"));
                Material material;
                if (oreNames.contains(args[1].toUpperCase())) {
                    material = Material.matchMaterial(args[1].toUpperCase());
                } else {
                    sendHelpMessage(player, message("oreCommandUsage"), "/ore get ");
                    return;
                }
                int requested;
                int balance = (int) economy.getBalance(player);
                try {
                    if (args[2].equalsIgnoreCase("all")) requested = balance;
                    else requested = stackToInteger(args[2]);
                    if (requested <= 0) throw new Exception();
                } catch (Exception e) {
                    player.sendMessage(deserialize(player, message("invalidPrice")));
                    return;
                }
                if (requested <= balance) {
                    HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(new ItemStack(Objects.requireNonNull(material), requested));
                    if (!overflow.isEmpty()) {
                        for (ItemStack item : overflow.values()) {
                            economy.withdrawPlayer(player, (requested - item.getAmount()) * getConfigInt("oreMoney.settings.oreMultiplier"));
                            player.sendMessage(deserialize(player, message("getSuccess").replace("<count>", integerToStack(requested - item.getAmount()))));
                            player.sendMessage(deserialize(player, message("inventoryIsFull").replace("<count>", integerToStack(item.getAmount()))));
                            return;
                        }
                    } else {
                        economy.withdrawPlayer(player, requested * getConfigInt("oreMoney.settings.oreMultiplier"));
                        player.sendMessage(deserialize(player, message("getSuccess").replace("<count>", integerToStack(requested))));
                    }
                } else {
                    player.sendMessage(deserialize(player, message("lackOfAr").replace("<count>", integerToStack(requested - balance))));
                }
            }
        } else {
            sendHelpMessage(player, message("oreCommandUsage"), "/ore ");
        }
    }

    public static List<Material> stringListToMaterialList(List<String> stringList) {
        List<Material> materials = new ArrayList<>();
        for (String name : stringList) {
            materials.add(Material.matchMaterial(name));
        }
        return materials;
    }

    public static int stackToInteger(String number) {
        String separator = ".";
        if (number.contains(separator)) {
            float numberFloat = Float.parseFloat(number);
            int numberInteger = (int) numberFloat * 64;
            int numberFractional = Integer.parseInt(number.substring(number.indexOf(separator) + separator.length()));
            return numberInteger + numberFractional;
        } else {
            return Integer.parseInt(number);
        }
    }

    public static String integerToStack(int number) {
        if (number >= 64) {
            double numberFormatted = number / 64.0;
            int numberInteger = (int) numberFormatted;
            int numberFractional = number - numberInteger * 64;
            return message("balanceFormat").replace("<integer>", String.valueOf(numberInteger))
                    .replace("<fractional>", String.valueOf(numberFractional));
        } else {
            return String.valueOf(number);
        }
    }

    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("balance", "sell", "get", "autosell", "pickupsell", "help");
        if ((args[0].equalsIgnoreCase("sell") && args.length == 2) || (args[0].equalsIgnoreCase("get") && args.length == 3)) {
            return Lists.newArrayList("all", "<amount>");
        }
        if (args[0].equalsIgnoreCase("get") && args.length == 2) {
            List<String> oreNames = new ArrayList<>(getConfigStringList("oreMoney.settings.sellItem"));
            List<String> oreNamesLowerCase = new ArrayList<>();
            for (String name : oreNames) {
                oreNamesLowerCase.add(name.toLowerCase());
            }
            return Lists.newArrayList(oreNamesLowerCase);
        }
        return Lists.newArrayList();
    }
}