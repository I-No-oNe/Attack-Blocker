package net.i_no_am.attack_blocker.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.attack_blocker.config.Config;
import net.minecraft.client.MinecraftClient;

import java.util.concurrent.CompletableFuture;


public class CommandSuggestions {

    public static class ColorSuggestions implements SuggestionProvider<FabricClientCommandSource> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
            for (Config.Color color : Config.Color.values()) {
                builder.suggest(color.toString().toLowerCase());
            }
            return builder.buildFuture();
        }
    }

    public static class PlayerSuggestions implements SuggestionProvider<FabricClientCommandSource> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
            Config.getBlockedPlayers().forEach(builder::suggest);
            return builder.buildFuture();
        }
    }

    public static class NetworkSuggestions implements SuggestionProvider<FabricClientCommandSource> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {

            var handler = MinecraftClient.getInstance().getNetworkHandler();
            if (handler == null) return builder.buildFuture();

            handler.getPlayerList().forEach(entry -> builder.suggest(entry.getProfile().name()));

            return builder.buildFuture();
        }
    }
}
