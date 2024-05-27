package net.i_no_am.damf.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.damf.config.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import static net.i_no_am.damf.DontAttackMyFriends.PREFIX;

public class CantAttackCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("cant-attack")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(context -> toggleCantAttack(context.getSource(), StringArgumentType.getString(context, "player")))));

        dispatcher.register(ClientCommandManager.literal("remove")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(context -> removeCantAttack(context.getSource(), StringArgumentType.getString(context, "player")))));

        dispatcher.register(ClientCommandManager.literal("damf")
                .then(ClientCommandManager.literal("on")
                        .executes(context -> setEnabled(context.getSource(), true)))
                .then(ClientCommandManager.literal("off")
                        .executes(context -> setEnabled(context.getSource(), false))));
    }

    private static int toggleCantAttack(FabricClientCommandSource source, String playerName) {
        ModConfig config = ModConfig.getInstance();
        String playerNameLower = playerName.toLowerCase();

        if (config.isEnabled()) {
            if (config.getBlockedPlayers().contains(playerNameLower)) {
                config.blockPlayer(playerNameLower);
                sendFeedback(PREFIX + "Player " + playerName + " added to can't attack list.", source);
            }
        } else {
            sendFeedback(PREFIX + "damf is currently disabled.", source);
        }
        return 1;
    }

    private static int removeCantAttack(FabricClientCommandSource source, String playerName) {
        ModConfig config = ModConfig.getInstance();
        String playerNameLower = playerName.toLowerCase();

        if (config.isEnabled()) {
            if (config.getBlockedPlayers().contains(playerNameLower)) {
                config.unblockPlayer(playerNameLower);
                sendFeedback(PREFIX + "Player " + playerName + " removed from can't attack list.", source);
            } else {
                sendFeedback(PREFIX + "Player " + playerName + " is not on the can't attack list.", source);
            }
        } else {
            sendFeedback(PREFIX + "damf is currently disabled.", source);
        }
        return 1;
    }

    private static int setEnabled(FabricClientCommandSource source, boolean enabled) {
        ModConfig config = ModConfig.getInstance();
        config.setEnabled(enabled);
        sendFeedback(PREFIX + "damf has been " + (enabled ? "enabled" : "disabled") + ".", source);
        return 1;
    }

    public static boolean canAttack(PlayerEntity attacker, PlayerEntity target) {
        ModConfig config = ModConfig.getInstance();
        String targetName = target.getName().getString().toLowerCase();
        return config.isEnabled() && !config.getBlockedPlayers().contains(targetName);
    }

    private static void sendFeedback(String msg, FabricClientCommandSource source) {
        source.sendFeedback(Text.of(msg));
    }
}
