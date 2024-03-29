package de.sprax2013.lime.spigot.nms;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NmsVersionDetector {
    private static final Map<String, String> VERSION_TO_REVISION;

    static {
        VERSION_TO_REVISION = new HashMap<>();
        VERSION_TO_REVISION.put("1.20", "v1_20_R1");
        VERSION_TO_REVISION.put("1.20.1", "v1_20_R1");
        VERSION_TO_REVISION.put("1.20.2", "v1_20_R2");
        VERSION_TO_REVISION.put("1.20.3", "v1_20_R3");
        VERSION_TO_REVISION.put("1.20.4", "v1_20_R3");
    }

    public static Optional<String> detect() {
        try {
            return Optional.of(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
        } catch (Exception ex) {
            String minecraftVersion = Bukkit.getServer().getBukkitVersion().split("-")[0];
            return Optional.ofNullable(VERSION_TO_REVISION.get(minecraftVersion));
        }
    }
}
