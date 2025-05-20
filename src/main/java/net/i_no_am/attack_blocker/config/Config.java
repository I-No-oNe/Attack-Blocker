package net.i_no_am.attack_blocker.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDirectory(), "attack-blocker");
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "attack-blocker.json");

    private boolean enabled = true;

    private Map<String, String> playerColors;
    private static Config instance = new Config();

    public static void getInstance() {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }
            if (CONFIG_FILE.exists()) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    instance = GSON.fromJson(reader, Config.class);
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

    public static Color getRGBA(String color) {
        return switch (color.toLowerCase()) {
            case "red" -> new Color(255, 0, 0, 255);
            case "green" -> new Color(0, 255, 0, 255);
            case "blue" -> new Color(0, 0, 255, 255);
            case "yellow" -> new Color(255, 255, 0, 255);
            case "white" -> new Color(255, 255, 255, 255);
            case "black" -> new Color(0, 0, 0, 255);
            case "orange" -> new Color(255, 165, 0, 255);
            case "purple" -> new Color(128, 0, 128, 255);
            case "cyan" -> new Color(0, 255, 255, 255);
            case "none" -> new Color(0, 0, 0, 0);
            default -> throw new IllegalStateException("Unexpected value: " + color.toLowerCase());
        };
    }
}
