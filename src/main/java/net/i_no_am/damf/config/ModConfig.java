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

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File configDir = new File(FabricLoader.getInstance().getConfigDirectory(), "damf");
    private static final File configFile = new File(configDir, "dont_attack_my_friends.json");
    private static ModConfig instance;

    private boolean enabled = true;
    private final Set<String> blockedPlayers = new HashSet<>();

    private ModConfig() {}

    public static ModConfig getInstance() {
        if (instance == null) {
            instance = new ModConfig();
            instance.loadConfig();
        }
        return instance;
    }

    private void loadConfig() {
        try {
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    instance = GSON.fromJson(reader, ModConfig.class);
                }
            } else {
                saveConfig();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle properly
        }
    }

    public void saveConfig() {
        try {
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle properly
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        saveConfig();
    }

    public Set<String> getBlockedPlayers() {
        return blockedPlayers;
    }

    public void blockPlayer(String playerName) {
        blockedPlayers.add(playerName);
        saveConfig();
    }

    public void unblockPlayer(String playerName) {
        blockedPlayers.remove(playerName);
        saveConfig();
    }
}
