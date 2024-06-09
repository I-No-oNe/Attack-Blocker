// Configuration.java

package net.i_no_am.attackblocker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Configuration {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDirectory(), "attack-blocker");
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "attack-blocker.json");

    private boolean enabled = true;

    // Store color set by command
    private Map<String, String> playerColors = new HashMap<>();

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
                    if (instance.playerColors == null) {
                        instance.playerColors = new HashMap<>();
                    }
                }
            } else {
                saveConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
        return instance.playerColors.keySet();
    }

    public static void blockPlayer(String playerName, String color) {
        instance.playerColors.put(playerName, color);
        saveConfig();
    }

    public static void unblockPlayer(String playerName) {
        instance.playerColors.remove(playerName);
        saveConfig();
    }

    public static boolean isPlayerBlocked(String playerName) {
        return instance.playerColors.containsKey(playerName);
    }

    public static String getPlayerColor(String playerName) {
        return instance.playerColors.getOrDefault(playerName, "none");
    }

    public static int[] getRGB(String color) {
        return switch (color.toLowerCase()) {
            case "red" -> new int[]{255, 0, 0, 255};
            case "green" -> new int[]{0, 255, 0, 255};
            case "blue" -> new int[]{0, 0, 255, 255};
            case "yellow" -> new int[]{255, 255, 0, 255};
            case "white" -> new int[]{255, 255, 255, 255};
            case "black" -> new int[]{0, 0, 0, 255};
            case "orange" -> new int[]{255, 165, 0, 255};
            case "purple" -> new int[]{128, 0, 128, 255};
            case "cyan" -> new int[]{0, 255, 255, 255};
            case "none" -> new int[]{0, 0, 0, 0};
            default -> throw new IllegalStateException("Unexpected value: " + color.toLowerCase());
        };
    }
}
