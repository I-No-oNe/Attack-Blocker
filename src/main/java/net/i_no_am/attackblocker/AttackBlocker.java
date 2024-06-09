package net.i_no_am.attackblocker;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.i_no_am.attackblocker.config.Configuration;

public class AttackBlocker implements ModInitializer {

	@Override
	public void onInitialize() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> Configuration.getInstance());
	}
}