    package net.i_no_am.attackblocker.command;

    import com.mojang.brigadier.CommandDispatcher;
    import com.mojang.brigadier.arguments.StringArgumentType;
    import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
    import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
    import net.i_no_am.attackblocker.config.Configuration;
    import net.i_no_am.attackblocker.utils.Utils;

    import java.util.Set;

    public class CantAttackCommand {

        public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(ClientCommandManager.literal("attack-blocker")
//                    Adding Players to Can't Attack list
                    .then(ClientCommandManager.literal("add")
                            .then(ClientCommandManager.argument("player name", StringArgumentType.word())
                                    .suggests(Utils.playerNameSuggestions())
                                    .executes(context -> addCantAttack(context.getSource(), StringArgumentType.getString(context, "player name")))))
//                    Removing Players from Can't Attack List
                    .then(ClientCommandManager.literal("remove")
                            .then(ClientCommandManager.argument("player name", StringArgumentType.word())
                                    .suggests(Utils.playerNameSuggestions())
                                    .executes(context -> removeCantAttack(context.getSource(), StringArgumentType.getString(context, "player name")))))
//                    Color Command
                    .then(ClientCommandManager.literal("color")
                            .then(ClientCommandManager.argument("player name", StringArgumentType.word())
                                    .suggests(Utils.playerNameSuggestions())
                                    .then(ClientCommandManager.argument("color", StringArgumentType.word())
                                            .suggests(Utils.colorSuggestions())
                                            .executes(context -> setColor(context.getSource(), StringArgumentType.getString(context, "player name"), StringArgumentType.getString(context, "color"))))))
//                    Show Players on list Command
                    .then(ClientCommandManager.literal("list")
                            .executes(context -> showCantAttackList(context.getSource())))
//                    On\Off Command
                    .then(ClientCommandManager.literal("on")
                            .executes(context -> setEnabled(context.getSource(), true)))
                    .then(ClientCommandManager.literal("off")
                            .executes(context -> setEnabled(context.getSource(), false))));
        }

        private static int addCantAttack(FabricClientCommandSource source, String playerName, String color) {
            if (Configuration.isEnabled()) {
                if (Configuration.getBlockedPlayers().contains(playerName)) {
                    Utils.clientMessage("Player " + playerName + " is already on the can't attack list.", source);
                } else {
                    Configuration.blockPlayer(playerName, color);
                    Utils.clientMessage("Player " + playerName + " added to can't attack list.", source);
                }
            } else {
                Utils.clientMessage("Attack blocker is disabled.", source);
            }
            return 1;
        }

        private static int addCantAttack(FabricClientCommandSource source, String playerName) {
            return addCantAttack(source, playerName, "none");
        }

        private static int removeCantAttack(FabricClientCommandSource source, String playerName) {
            if (Configuration.isEnabled()) {
                if (Configuration.getBlockedPlayers().contains(playerName)) {
                    Configuration.unblockPlayer(playerName);
                    Utils.clientMessage("Player " + playerName + " removed from can't attack list.", source);
                } else {
                    Utils.clientMessage("Player " + playerName + " is not on the can't attack list.", source);
                }
            } else {
                Utils.clientMessage("Attack blocker is disabled.", source);
            }
            return 1;
        }

        private static int setEnabled(FabricClientCommandSource source, boolean enabled) {
            Configuration.setEnabled(enabled);
            Utils.clientMessage("Attack blocker is " + (enabled ? "enabled" : "disabled") + ".", source);
            return 1;
        }

        private static int setColor(FabricClientCommandSource source, String playerName, String color) {
            if (Configuration.isEnabled()) {
                if (Configuration.getBlockedPlayers().contains(playerName)) {
                    if (Utils.COLORS.contains(color)) {
                        Configuration.blockPlayer(playerName, color);
                        Utils.clientMessage("Color for player " + playerName + " set to " + color + ".", source);
                    } else {
                        Configuration.blockPlayer(playerName, "none");
                        Utils.clientMessage("Invalid color. Player " + playerName + " will not have a specific color.", source);
                    }
                } else {
                    Utils.clientMessage("Player " + playerName + " is not on the can't attack list.", source);
                }
            } else {
                Utils.clientMessage("Attack blocker is disabled.", source);
            }
            return 1;
        }

        private static int showCantAttackList(FabricClientCommandSource source) {
            if (Configuration.isEnabled()) {
                Set<String> blockedPlayers = Configuration.getBlockedPlayers();
                if (blockedPlayers.isEmpty()) {
                    Utils.clientMessage("No players are on the can't attack list.", source);
                } else {
                    Utils.clientMessage("Players on the can't attack list:", source);
                    for (String playerName : blockedPlayers) {
                        Utils.clientMessageWithoutPrefix("- " + playerName, source);
                    }
                }
            } else {
                Utils.clientMessage("Attack blocker is disabled.", source);
            }
            return 1;
        }
    }