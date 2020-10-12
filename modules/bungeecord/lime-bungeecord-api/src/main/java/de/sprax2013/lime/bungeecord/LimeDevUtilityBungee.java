package de.sprax2013.lime.bungeecord;

import de.sprax2013.lime.LimeDevUtility;
import de.sprax2013.lime.bungeecord.bstats.MetricsLite;
import net.md_5.bungee.api.plugin.Plugin;

// TODO: Add UpdateChecker using Level WARNING
public class LimeDevUtilityBungee {
    private static boolean initialized = false;

    private LimeDevUtilityBungee() {
        throw new IllegalStateException("Utility class");
    }

    public static void init(Plugin plugin) {
        if (initialized) return;

        initialized = true;

        LimeDevUtility.LOGGER.setParent(plugin.getLogger());

        try {
            new MetricsLite(plugin);
        } catch (Exception ex) {
            LimeDevUtility.LOGGER.warning("Could not load bStats (" + ex.getClass().getSimpleName() + "): " + ex.getMessage());
        }
    }
}