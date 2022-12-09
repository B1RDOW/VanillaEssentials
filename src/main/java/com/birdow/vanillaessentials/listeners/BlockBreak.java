package com.birdow.vanillaessentials.listeners;

import com.birdow.vanillaessentials.hooks.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.birdow.vanillaessentials.commands.Ore.stringListToMaterialList;
import static com.birdow.vanillaessentials.utils.ConfigManager.*;
import static com.birdow.vanillaessentials.utils.Messages.message;
import static com.birdow.vanillaessentials.utils.Messages.sendActionBarMessage;

public class BlockBreak implements Listener {

    @EventHandler
    public void blockBreak (BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (getPlayerDataBoolean(player.getName() + ".oreAutoSell")) {
            List<Material> oreMaterials = new ArrayList<>(stringListToMaterialList(getConfigStringList("oreMoney.settings.sellItem")));
            if (!oreMaterials.contains(block.getType())) return;
            if (!getConfigBoolean("oreMoney.settings.breakInCreative")) {
                if (player.getGameMode() == GameMode.CREATIVE) return;
            }
            if (getConfigBoolean("oreMoney.settings.needEnchantment")) {
                if (!player.getInventory().getItemInMainHand().containsEnchantment(Objects.requireNonNull(Enchantment.getByKey(NamespacedKey.minecraft(getConfigString("oreMoney.settings.enchantmentType").toLowerCase()))))) return;
            }
            Economy economy = Vault.getEconomy();
            event.setDropItems(false);
            economy.depositPlayer(player, getConfigInt("oreMoney.settings.oreMultiplier"));
            sendActionBarMessage(player, message("autoAdd").replace("<count>", String.valueOf(getConfigInt("oreMoney.settings.oreMultiplier"))));
        }
    }
}