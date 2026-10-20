package net.i_no_am.attack_blocker.utils;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class ChatUtils {

    private static final String PREFIX = "§7[§4Attack-Blocker§7]§r ";

    public static void message(String message, FabricClientCommandSource source) {
        source.sendFeedback(Component.literal(PREFIX + message));
    }

    public static void messagePrefix(String message, FabricClientCommandSource source) {
        source.sendFeedback(Component.literal(message));
    }
}
