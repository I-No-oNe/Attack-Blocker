package net.i_no_am.damf;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.i_no_am.damf.command.CantAttackCommand;
import net.i_no_am.damf.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DontAttackMyFriends implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger("DontAttackMyFriends");
	public static final String PREFIX = "§7[§4DontAttackMyFriends§7]§r ";

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> registerClientCommands(dispatcher));  // Client Commands
		ModConfig.getInstance().saveConfig();
	}
		public static void registerClientCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
			CantAttackCommand.register(dispatcher);
		LOGGER.info("DontAttackMyFriends initialized.");
	}
}
/** TODO:
 * - Fix command logic and config.
 * - make it so lower case won't matter (and vice versa)
 * - Make players who are on the list glow on red
 * - Load the config EVERY TICK!!!!
*  - Bonus - add improperUi support
*  */