package net.i_no_am.attackblocker.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.attackblocker.config.Configuration;
import net.i_no_am.attackblocker.utils.Utils;

public class CantAttackCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("attack-blocker")
                .then(ClientCommandManager.literal("add")
                        .then(ClientCommandManager.argument("player name", StringArgumentType.word())
                                .executes(context -> addCantAttack(context.getSource(), StringArgumentType.getString(context, "player name")))))
                .then(ClientCommandManager.literal("remove")
                        .then(ClientCommandManager.argument("player name", StringArgumentType.word())
                                .executes(context -> removeCantAttack(context.getSource(), StringArgumentType.getString(context, "player name")))))
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
            Utils.clientMessage("Attack blocker is disabled.", source);
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
            Utils.clientMessage("is disabled.", source);
        }
        return 1;
    }

    private static int setEnabled(FabricClientCommandSource source, boolean enabled) {
        Configuration.setEnabled(enabled);
        Utils.clientMessage("is " + (enabled ? "enabled" : "disabled") + ".", source);
        return 1;
    }
}
