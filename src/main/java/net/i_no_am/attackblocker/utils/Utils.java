package net.i_no_am.attackblocker.utils;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.attackblocker.config.Configuration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class Utils {
    public static final List<String> COLORS = List.of("red", "green", "blue", "yellow", "white", "black", "orange", "purple", "cyan", "none");

    public static final String PREFIX = "§7[§4Attack-Blocker§7]§r ";

    public static ClientPlayerEntity attacker = MinecraftClient.getInstance().player;

    public static boolean mixinEnabled = false;

    public static void clientMessage(String message, FabricClientCommandSource source) {
        source.sendFeedback(Text.of(PREFIX + message));
    }

    public static void clientMessageWithoutPrefix(String message, FabricClientCommandSource source) {
        source.sendFeedback(Text.of(message));
    }

    public static void playerMessage(String message, ClientPlayNetworkHandler clientPlayNetworkHandler) {
        clientPlayNetworkHandler.sendChatMessage(message);
    }

    public static boolean canAttack(PlayerEntity ignoreattacker, PlayerEntity target) {
        String targetName = target.getName().getString();
        return Configuration.isEnabled() && !Configuration.getBlockedPlayers().contains(targetName);
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
            Configuration.getBlockedPlayers().forEach(builder::suggest);
            return builder.buildFuture();
        };
}
    public static SuggestionProvider<FabricClientCommandSource> playerNameSuggestions() {
        return (context, builder) -> {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerList().forEach(entry -> {
                String playerName = entry.getProfile().getName();
                builder.suggest(playerName);
            });
            return builder.buildFuture();
        };
    }
    public static void setMixinEnabled(boolean fr) {
        mixinEnabled = fr;
    }
    public static boolean isMixinEnabled() {
        return mixinEnabled;
    }
}
