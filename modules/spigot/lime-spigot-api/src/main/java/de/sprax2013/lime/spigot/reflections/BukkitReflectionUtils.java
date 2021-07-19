package de.sprax2013.lime.spigot.reflections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("unused")
@Deprecated
public class BukkitReflectionUtils {
    private static String version;

    private BukkitReflectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Example output: <code>v1_16_R3</code>
     *
     * @return The Bukkit-Version of the server
     */
    @NotNull
    public static String getBukkitVersion() {
        if (version == null) {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }

        return version;
    }

    public static int getPlayerPing(@NotNull Player p) throws ReflectiveOperationException {
        Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
        return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
    }

    /**
     * Sends a packet to players using Reflections.<br>
     * If an Exception occurs, it is printed to the console.
     *
     * @param players The players
     * @param packet  The packet
     *
     * @throws ReflectiveOperationException .
     * @see #sendPacket(Player, Object)
     */
    public static void sendPacket(Collection<? extends Player> players, Object packet) throws ReflectiveOperationException {
        for (Player p : players) {
            Object handle = p.getClass().getMethod("getHandle").invoke(p);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);

            playerConnection.getClass()
                    .getMethod("sendPacket", getNMSClass("net.minecraft.server.?.Packet"))
                    .invoke(playerConnection, packet);
        }
    }

    /**
     * Sends a packet to a player using Reflections.<br>
     *
     * @param player The player to send the packet to
     * @param packet The packet to send
     *
     * @throws ReflectiveOperationException .
     * @see #sendPacket(Collection, Object)
     */
    public static void sendPacket(Player player, Object packet) throws ReflectiveOperationException {
        sendPacket(Collections.singletonList(player), packet);
    }

    /**
     * Returns an NMS-Class.<br>
     * Use '<code>?</code>' to let it replaced by the Bukkit-Server-Version
     *
     * @param name The class name (example: net.minecraft.server.?.Packet)
     *
     * @return the NMS class
     *
     * @throws ClassNotFoundException .
     */
    @NotNull
    public static Class<?> getNMSClass(@NotNull String name) throws ClassNotFoundException {
        return Class.forName(name.replace("?", getBukkitVersion()));
    }

    /**
     * Returns an NMS-Class.<br>
     * Use '<code>?</code>' to let it replaced by the Bukkit-Server-Version
     *
     * @param name The class name (example: net.minecraft.server.?.Packet)
     *
     * @return The NMS class or null
     */
    @Nullable
    public static Class<?> getNMSClassNullable(@NotNull String name) {
        try {
            return getNMSClass(name);
        } catch (ClassNotFoundException ignore) {
        }

        return null;
    }

    /**
     * Sets the value of an field.<br>
     * Example usage would be, when creating a packet.
     *
     * @param obj       The Object that contains the field to modify
     * @param fieldName The name of the field that should be modified
     * @param value     The Value that should be set
     *
     * @throws NoSuchFieldException   .
     * @throws IllegalAccessException .
     */
    public static void setValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);

        boolean oldAccess = field.isAccessible();

        if (!oldAccess) {
            field.setAccessible(true);
        }

        field.set(obj, value);

        if (!oldAccess) {
            field.setAccessible(false);
        }
    }

    /**
     * Gets the value of an field.<br>
     * Example usage would be, when creating a packet.
     *
     * @param obj       The Object that contains the field to get the value from
     * @param fieldName The name of the field
     *
     * @return The value of the field
     *
     * @throws NoSuchFieldException   .
     * @throws IllegalAccessException .
     */
    public static Object getValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);

        boolean oldAccess = field.isAccessible();

        if (!oldAccess) {
            field.setAccessible(true);
        }

        Object result = field.get(obj);

        if (!oldAccess) {
            field.setAccessible(false);
        }

        return result;
    }

    public static byte getFixAngle(float angle) {
        return (byte) (angle * 256 / 360);
    }

    public static int getFixPoint(double nonFixPoint) {
        return (int) (nonFixPoint * 32.0D);
    }

    public static double fromFixPoint(int fixPoint) {
        return fixPoint / 32.0D;
    }
}
