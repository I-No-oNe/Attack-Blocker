package net.i_no_am.attack_blocker.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.attack_blocker.config.Config;
import net.i_no_am.attack_blocker.utils.ChatUtils;
import net.minecraft.command.CommandRegistryAccess;

import java.util.Set;

public class CantAttackCommand implements ClientCommandRegistrationCallback {

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> commandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        commandDispatcher.register(ClientCommandManager.literal("attack-blocker")
//                    Adding Players to Can't Attack list
                .then(ClientCommandManager.literal("add").then(ClientCommandManager.argument("player name", StringArgumentType.word()).suggests(new CommandSuggestions.NetworkSuggestions()).executes(context -> addCantAttack(context.getSource(), StringArgumentType.getString(context, "player name")))))
//                    Removing Players from Can't Attack List
                .then(ClientCommandManager.literal("remove").then(ClientCommandManager.argument("player name", StringArgumentType.word()).suggests(new CommandSuggestions.PlayerSuggestions()).executes(context -> removeCantAttack(context.getSource(), StringArgumentType.getString(context, "player name")))))
//                    Color Command
                .then(ClientCommandManager.literal("color").then(ClientCommandManager.argument("player name", StringArgumentType.word()).suggests(new CommandSuggestions.PlayerSuggestions()).then(ClientCommandManager.argument("color", StringArgumentType.word()).suggests(new CommandSuggestions.ColorSuggestions()).executes(context -> setColor(context.getSource(), StringArgumentType.getString(context, "player name"), StringArgumentType.getString(context, "color"))))))
//                    Show Players on list Command
                .then(ClientCommandManager.literal("list").executes(context -> showCantAttackList(context.getSource())))
//                    On\Off Command
                .then(ClientCommandManager.literal("on").executes(context -> setEnabled(context.getSource(), true))).then(ClientCommandManager.literal("off").executes(context -> setEnabled(context.getSource(), false))));
    }

    private static int addCantAttack(FabricClientCommandSource source, String playerName) {
        if (!Config.isEnabled()) {
            ChatUtils.message("Attack Block is Disabled", source);
            return Command.SINGLE_SUCCESS;
        }

        if (Config.getBlockedPlayers().contains(playerName)) {
            ChatUtils.message("Player " + playerName + " is already on the can't attack list.", source);
        } else {
            Config.blockPlayer(playerName, "none");
            ChatUtils.message("Player " + playerName + " added to can't attack list.", source);
        }
        return Command.SINGLE_SUCCESS;
    }


    private static int removeCantAttack(FabricClientCommandSource source, String playerName) {
        if (!Config.isEnabled()) {
            ChatUtils.message("Attack blocker is disabled.", source);
            return Command.SINGLE_SUCCESS;
        }

        if (!Config.getBlockedPlayers().contains(playerName)) {
            ChatUtils.message("Player " + playerName + " is not on the can't attack list.", source);
            return Command.SINGLE_SUCCESS;
        }

        Config.unblockPlayer(playerName);
        ChatUtils.message("Player " + playerName + " removed from can't attack list.", source);
        return Command.SINGLE_SUCCESS;
    }

    private static int setEnabled(FabricClientCommandSource source, boolean enabled) {
        Config.setEnabled(enabled);
        ChatUtils.message("Attack blocker is " + (enabled ? "enabled" : "disabled") + ".", source);
        return Command.SINGLE_SUCCESS;
    }

    private static int setColor(FabricClientCommandSource source, String playerName, String color) {
        if (!Config.isEnabled()) {
            ChatUtils.message("Attack blocker is disabled.", source);
            return Command.SINGLE_SUCCESS;
        }

        if (!Config.getBlockedPlayers().contains(playerName)) {
            ChatUtils.message("Player " + playerName + " is not on the can't attack list.", source);
            return Command.SINGLE_SUCCESS;
        }

        if (!Config.Color.contain(color)) {
            Config.blockPlayer(playerName, "none");
            ChatUtils.message("Invalid color. Player " + playerName + " will not have a specific color.", source);
            return Command.SINGLE_SUCCESS;
        }

        Config.blockPlayer(playerName, color);
        ChatUtils.message("Color for player " + playerName + " set to " + color + ".", source);
        return Command.SINGLE_SUCCESS;
    }

    private static int showCantAttackList(FabricClientCommandSource source) {
        if (!Config.isEnabled()) {
            ChatUtils.message("Attack blocker is disabled.", source);
            return Command.SINGLE_SUCCESS;
        }

        Set<String> blockedPlayers = Config.getBlockedPlayers();
        if (blockedPlayers.isEmpty()) {
            ChatUtils.message("No players are on the can't attack list.", source);
            return Command.SINGLE_SUCCESS;
        }

        ChatUtils.message("Players on the can't attack list:", source);
        for (String playerName : blockedPlayers) {
            ChatUtils.messagePrefix("- " + playerName, source);
        }
        return Command.SINGLE_SUCCESS;
    }
}