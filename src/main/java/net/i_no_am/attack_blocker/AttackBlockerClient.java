package net.i_no_am.attack_blocker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.i_no_am.attack_blocker.command.CantAttackCommand;
import net.i_no_am.attack_blocker.config.Config;

public class AttackBlockerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(new CantAttackCommand());
        Config.loadConfig();
    }
}