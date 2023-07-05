package de.silentesc.tpa;

import de.silentesc.tpa.utils.ConfigUtils;
import de.silentesc.tpa.utils.JavaUtils;
import de.silentesc.tpa.utils.ShortMessages;

import java.io.File;

public class Manager {
    /*
     * Manager for instances, register commands and load configs
     * Used for example to init everything at the plugin start like prefix, utils and so on
     * You can get all utils from this class
     */

    // Global variables
    private String prefix;
    // Util instances
    private ShortMessages shortMessages;
    private ConfigUtils configUtils;
    private JavaUtils javaUtils;

    public Manager() {
        loadConfig();
        initialize();
        register();
    }

    // Create config.yaml if it doesn't exist
    private void loadConfig() {
        String configPath = "config.yaml";
        File file = new File(Main.getInstance().getDataFolder().getPath() + "/" + configPath);
        if (!file.exists()) Main.getInstance().saveResource(configPath, false);
    }

    // Init all classes like utils etc.
    private void initialize() {
        prefix = "§7[§eTPA§7] ";
        shortMessages = new ShortMessages();
        configUtils = new ConfigUtils(); // Init FileConfig: config.yaml
        javaUtils = new JavaUtils();
    }

    // Register Commands, TabCompleter and Listeners
    private void register() {

    }

    // Getter
    public String getPrefix() {
        return prefix;
    }
    public ShortMessages getShortMessages() {
        return shortMessages;
    }
    public ConfigUtils getConfigUtils() {
        return configUtils;
    }
    public JavaUtils getJavaUtils() {
        return javaUtils;
    }
}