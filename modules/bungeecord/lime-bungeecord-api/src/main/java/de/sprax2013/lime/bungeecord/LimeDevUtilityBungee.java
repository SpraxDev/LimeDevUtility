package de.sprax2013.lime.bungeecord;

import de.sprax2013.lime.LimeDevUtility;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Level;

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
            new Metrics(plugin);
        } catch (Exception ex) {
            LimeDevUtility.LOGGER.log(Level.WARNING, "Could not initialize bStats", ex);
        }
    }
}