package net.i_no_am.attack_blocker.utils;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.attack_blocker.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class Utils {
    public static final List<String> COLORS = List.of("red", "green", "blue", "yellow", "white", "black", "orange", "purple", "cyan", "none");

    public static final String PREFIX = "§7[§4Attack-Blocker§7]§r ";

    public static void clientMessage(String message, FabricClientCommandSource source) {
        source.sendFeedback(Text.of(PREFIX + message));
    }

    public static void clientMessageWithoutPrefix(String message, FabricClientCommandSource source) {
        source.sendFeedback(Text.of(message));
    }

    @Deprecated(forRemoval = true)
    public static void playerMessage(String message, ClientPlayNetworkHandler clientPlayNetworkHandler) {
        clientPlayNetworkHandler.sendChatMessage(message);
    }

    public static boolean cannotAttack(PlayerEntity target) {
        String targetName = target.getName().getString();
        return Config.getBlockedPlayers().contains(targetName);
    }

    public static SuggestionProvider<FabricClientCommandSource> colorSuggestions() {
        return (context, builder) -> {
            for (String color : COLORS) {
                builder.suggest(color);
            }
            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<FabricClientCommandSource> playersInConfig() {
        return (context, builder) -> {
            Config.getBlockedPlayers().forEach(builder::suggest);
            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<FabricClientCommandSource> playerNameSuggestions() {
        return (context, builder) -> {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerList().forEach(entry -> {
                builder.suggest(entry.getProfile().getName());
            });
            return builder.buildFuture();
        };
    }
}
