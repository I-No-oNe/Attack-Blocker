package net.i_no_am.damf.utils;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.damf.config.Configuration;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class Utils {
    public static final String PREFIX = "§7[§4DontAttackMyFriends§7]§r ";

    public static void clientMessage(String message, FabricClientCommandSource source) {
        source.sendFeedback(Text.of(PREFIX + message));
    }
    public static void playerMessage(String message, ClientPlayNetworkHandler clientPlayNetworkHandler){
        clientPlayNetworkHandler.sendChatMessage(message);
    }
    public static boolean canAttack(PlayerEntity ignoreattacker, PlayerEntity target) {
        String targetName = target.getName().getString();
        return Configuration.isEnabled() && !Configuration.getBlockedPlayers().contains(targetName);
    }
}
