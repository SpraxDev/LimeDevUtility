package de.sprax2013.lime.spigot.reflections;

import org.bukkit.entity.Player;

public class GameProfileUtils {
    private GameProfileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Object getGameProfile(Player p) throws ReflectiveOperationException {
        return BukkitReflectionUtils.getNMSClass("org.bukkit.craftbukkit.?.entity.CraftPlayer")
                .getMethod("getProfile")
                .invoke(p);
    }
}