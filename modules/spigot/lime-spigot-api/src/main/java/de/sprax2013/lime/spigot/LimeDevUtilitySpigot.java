package de.sprax2013.lime.spigot;

import de.sprax2013.lime.LimeDevUtility;
import de.sprax2013.lime.spigot.bstats.MetricsLite;
import de.sprax2013.lime.spigot.third_party.de.sprax2013.advanced_dev_utils.events.CustomEventManager;
import org.bukkit.plugin.java.JavaPlugin;

// TODO: Add UpdateChecker using Level WARNING
public class LimeDevUtilitySpigot {
    private static JavaPlugin plugin;
    private static boolean initialized = false;

    private LimeDevUtilitySpigot() {
        throw new IllegalStateException("Utility class");
    }

    public static void init(JavaPlugin pluginInstance) {
        if (initialized) return;

        initialized = true;
        plugin = pluginInstance;

        LimeDevUtility.LOGGER.setParent(plugin.getLogger());

        try {
            // TODO: Add Custom ServerVersion-Pie that shows NMS-Versions when clicked
            new MetricsLite(plugin);
        } catch (Exception ex) {
            LimeDevUtility.LOGGER.warning("Could not load bStats (" + ex.getClass().getSimpleName() + "): " + ex.getMessage());
        }

        CustomEventManager.init(pluginInstance);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}