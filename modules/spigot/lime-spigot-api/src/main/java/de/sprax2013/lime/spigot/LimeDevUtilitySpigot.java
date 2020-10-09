package de.sprax2013.lime.spigot;

import de.sprax2013.lime.configuration.LimeDevUtility;
import de.sprax2013.lime.spigot.bstats.MetricsLite;
import org.bukkit.plugin.java.JavaPlugin;

// TODO: Add UpdateChecker using Level WARNING
public class LimeDevUtilitySpigot {
    private static boolean initialized = false;

    public static void init(JavaPlugin plugin) {
        if (initialized) return;

        initialized = true;

        LimeDevUtility.LOGGER.setParent(plugin.getLogger());

        try {
            // TODO: Add Custom ServerVersion-Pie that shows NMS-Versions when clicked
            new MetricsLite(plugin);
        } catch (Exception ex) {
            LimeDevUtility.LOGGER.warning("Could not load bStats (" + ex.getClass().getSimpleName() + "): " + ex.getMessage());
        }
    }
}