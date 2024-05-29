package net.i_no_am.damf.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.damf.config.Configuration;
import net.i_no_am.damf.utils.Utils;
import net.minecraft.entity.player.PlayerEntity;

public class CantAttackCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // Add player command
        dispatcher.register(ClientCommandManager.literal("cant-attack")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(context -> addCantAttack(context.getSource(), StringArgumentType.getString(context, "player")))));
        // Remove player command
        dispatcher.register(ClientCommandManager.literal("remove")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(context -> removeCantAttack(context.getSource(), StringArgumentType.getString(context, "player")))));
        // Toggle the mod command
        dispatcher.register(ClientCommandManager.literal("damf")
                .then(ClientCommandManager.literal("on")
                        .executes(context -> setEnabled(context.getSource(), true)))
                .then(ClientCommandManager.literal("off")
                        .executes(context -> setEnabled(context.getSource(), false))));
    }

    private static int addCantAttack(FabricClientCommandSource source, String playerName) {

        if (Configuration.isEnabled()) {
            if (Configuration.getBlockedPlayers().contains(playerName)) {
                Utils.clientMessage("Player " + playerName + " is already on the can't attack list.", source);
            } else {
                Configuration.blockPlayer(playerName);
                Utils.clientMessage("Player " + playerName + " added to can't attack list.", source);
            }
        } else {
            Utils.clientMessage("damf is currently disabled.", source);
        }
        return 1;
    }

    private static int removeCantAttack(FabricClientCommandSource source, String playerName) {
        if (Configuration.isEnabled()) {
            if (Configuration.getBlockedPlayers().contains(playerName)) {
                Configuration.unblockPlayer(playerName);
                Utils.clientMessage("Player " + playerName + " removed from can't attack list.", source);
            } else {
                Utils.clientMessage("Player " + playerName + " is not on the can't attack list.", source);
            }
        } else {
            Utils.clientMessage("damf is currently disabled.", source);
        }
        return 1;
    }

    private static int setEnabled(FabricClientCommandSource source, boolean enabled) {
        Configuration.setEnabled(enabled);
        Utils.clientMessage("damf has been " + (enabled ? "enabled" : "disabled") + ".", source);
        return 1;
    }
}
