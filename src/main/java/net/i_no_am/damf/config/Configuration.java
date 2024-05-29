package net.i_no_am.damf.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Configuration {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDirectory(), "damf");
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "dont_attack_my_friends.json");

    private static boolean enabled = true;
    private static final Set<String> blockedPlayers = new HashSet<>();

    static {
        loadConfig();
    }

    private Configuration() {}

    private static void loadConfig() {
        try {
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }
            if (CONFIG_FILE.exists()) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    Configuration config = GSON.fromJson(reader, Configuration.class);
                    enabled = config.enabled;
                    blockedPlayers.clear();
                    blockedPlayers.addAll(config.blockedPlayers);
                }
            } else {
                saveConfig();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle properly
        }
    }

    public static void saveConfig() {
        try {
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(new Configuration(), writer);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle properly
        }
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        Configuration.enabled = enabled;
        saveConfig();
    }

    public static Set<String> getBlockedPlayers() {
        return blockedPlayers;
    }

    public static void blockPlayer(String playerName) {
        blockedPlayers.add(playerName);
        saveConfig();
    }

    public static void unblockPlayer(String playerName) {
        blockedPlayers.remove(playerName);
        saveConfig();
    }
}
