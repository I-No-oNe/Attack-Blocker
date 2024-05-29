package net.i_no_am.damf;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.i_no_am.damf.config.Configuration;

public class DontAttackMyFriends implements ModInitializer {

	@Override
	public void onInitialize() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
					Configuration.saveConfig();
		});
	}
}
/** TODO:
 * - Fix  config.
 * - make it so lower case won't matter (and vice versa)
 * - Make players who are on the list glow on red
*  */