package com.birdow.vanillaessentials.commands;

import com.birdow.vanillaessentials.handlers.CustomCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.birdow.vanillaessentials.utils.ConfigManager.getConfigBoolean;
import static com.birdow.vanillaessentials.utils.Messages.*;

public class Enderchest extends CustomCommand {

    public Enderchest() { super("enderchest"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0){
            if (noPermission(player, "enderchest.open")) return;
            openEnderChest(player, player);
        }
        if (args.length > 0) {
            if (noPermission(player, "enderchest.open.target")) return;
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                openEnderChest(player, target);
            } else {
                player.sendMessage(deserialize(player, message("playerNotOnline").replace("<player>", args[0])));
            }
        }
    }

    private void openEnderChest(Player player, Player target) {
        player.openInventory(target.getEnderChest());
        if (getConfigBoolean("enderchest.openMessage")) {
            player.sendMessage(deserialize(player, message("enderchestOpen")));
        }
    }
}