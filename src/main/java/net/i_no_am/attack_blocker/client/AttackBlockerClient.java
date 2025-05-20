package net.i_no_am.attack_blocker.client;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.attack_blocker.command.CantAttackCommand;

public class AttackBlockerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> registerClientCommands(dispatcher));
    
    }
    public static void registerClientCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CantAttackCommand.register(dispatcher);
    }
}