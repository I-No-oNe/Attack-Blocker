package net.i_no_am.attack_blocker.config;

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

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDir().toFile(), "attack-blocker");
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "attack-blocker.json");

    private boolean enabled = true;

    private Map<String, String> playerColors;
    private static Config INSTANCE = new Config();

    public static void loadConfig() {
        try {
            if (!CONFIG_DIR.exists()) CONFIG_DIR.mkdirs();
            if (CONFIG_FILE.exists()) {
                try (FileReader reader = new FileReader(CONFIG_FILE)) {
                    INSTANCE = GSON.fromJson(reader, Config.class);
                    if (INSTANCE.playerColors == null) {
                        INSTANCE.playerColors = new HashMap<>();
                    }
                }
            } else {
                saveConfig();
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public static void saveConfig() {
        try {
            if (!CONFIG_DIR.exists()) CONFIG_DIR.mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public static boolean isEnabled() {
        return INSTANCE.enabled;
    }

    public static void setEnabled(boolean enabled) {
        INSTANCE.enabled = enabled;
        saveConfig();
    }

    public static int getColor(String colorName) {
        try {
            Config.Color color = Config.Color.valueOf(colorName.toUpperCase());
            return color.getRGBA();
        } catch (IllegalArgumentException e) {
            return Config.Color.NONE.getRGBA();
        }
    }

    public static Set<String> getBlockedPlayers() {
        return INSTANCE.playerColors.keySet();
    }

    public static void blockPlayer(String playerName, String color) {
        INSTANCE.playerColors.put(playerName, color);
        saveConfig();
    }

    public static void unblockPlayer(String playerName) {
        INSTANCE.playerColors.remove(playerName);
        saveConfig();
    }

    public static boolean isPlayerBlocked(String playerName) {
        return INSTANCE.playerColors.containsKey(playerName);
    }

    public static String getPlayerColor(String playerName) {
        return INSTANCE.playerColors.getOrDefault(playerName, "none");
    }

    public enum Color {
        RED(255, 0, 0, 255),
        GREEN(0, 255, 0, 255),
        BLUE(0, 0, 255, 255),
        YELLOW(255, 255, 0, 255),
        WHITE(255, 255, 255, 255),
        BLACK(0, 0, 0, 255),
        ORANGE(255, 165, 0, 255),
        PURPLE(128, 0, 128, 255),
        CYAN(0, 255, 255, 255),
        NONE(-1, -1, -1, -1);

        private final int red;
        private final int green;
        private final int blue;
        private final int alpha;

        Color(int red, int green, int blue, int alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public int getRGBA() {
            return alpha << 24 | red << 16 | green << 8 | blue;
        }

        public static boolean contain(String colorName) {
            for (Color color : values()) {
                if (color.name().equalsIgnoreCase(colorName)) {
                    return true;
                }
            }
            return false;
        }
    }
}
