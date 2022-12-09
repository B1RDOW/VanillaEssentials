package com.birdow.vanillaessentials.handlers;

import com.birdow.vanillaessentials.VanillaEssentials;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.birdow.vanillaessentials.utils.ConfigManager.configEnabled;
import static com.birdow.vanillaessentials.utils.Messages.*;

public abstract class CustomCommand implements CommandExecutor, TabCompleter {

    private String command;
    private String configCommand;
    private String permission;
    private boolean console = false;

    public CustomCommand(String command, String configCommand, Boolean canConsole, String permission) {
        setup(command);
        this.configCommand = configCommand;
        this.console = canConsole;
        this.permission = permission;
    }
    public CustomCommand(String command, String configCommand, Boolean canConsole) {
        setup(command);
        this.configCommand = configCommand;
        this.console = canConsole;
    }
    public CustomCommand(String command, Boolean canConsole, String permission) {
        setup(command);
        this.console = canConsole;
        this.permission = permission;
    }
    public CustomCommand(String command, Boolean canConsole) {
        setup(command);
        this.console = canConsole;
    }
    public CustomCommand(String command, String configCommand) {
        setup(command);
        this.configCommand = configCommand;
    }
    public CustomCommand(String command) {
        setup(command);
    }

    private void setup(String command) {
        PluginCommand pluginCommand = VanillaEssentials.getInstance().getCommand(command);
        if (pluginCommand == null) return;
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
        this.command = command;
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!configEnabled(this.configCommand == null ? this.command : this.configCommand)) {
            sender.sendMessage(deserialize(sender, message("disabledCommand")));
            return true;
        }
        if (!this.console && !(sender instanceof Player)) {
            sender.sendMessage(deserialize(sender, message("onlyPlayer")));
            return true;
        }
        if (this.permission != null) {
            if (noPermission(sender, this.permission)) return true;
        }
        execute(sender, label, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return filter(complete(sender, args), args);
    }

    private List<String> filter(List<String> list, String[] args) {
        if (list == null) return null;
        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();
        for (String arg : list) {
            if (arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);
        }
        return result;
    }
}