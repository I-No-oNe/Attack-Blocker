package net.i_no_am.attackblocker.config;

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
    private static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDirectory(), "attack-blocker");
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "attack-blocker.json");

    private boolean enabled = true;
    private Set<String> blockedPlayers = new HashSet<>();

    private static Configuration instance = new Configuration();

    static {
        loadConfig();
    }

    private Configuration() {}

    public static void getInstance() {
    }

    private static void loadConfig() {
        try {
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }
            if (CONFIG_FILE.exists()) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    instance = GSON.fromJson(reader, Configuration.class);
                    if (instance.blockedPlayers == null) {
                        instance.blockedPlayers = new HashSet<>();
                    }
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
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle properly
        }
    }

    public static boolean isEnabled() {
        return instance.enabled;
    }

    public static void setEnabled(boolean enabled) {
        instance.enabled = enabled;
        saveConfig();
    }

    public static Set<String> getBlockedPlayers() {
        return instance.blockedPlayers;
    }

    public static void blockPlayer(String playerName) {
        instance.blockedPlayers.add(playerName);
        saveConfig();
    }

    public static void unblockPlayer(String playerName) {
        instance.blockedPlayers.remove(playerName);
        saveConfig();
    }
}
