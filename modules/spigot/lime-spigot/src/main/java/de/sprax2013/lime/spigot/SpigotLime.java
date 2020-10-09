package de.sprax2013.lime.spigot;

import org.bukkit.plugin.java.JavaPlugin;

public class SpigotLime extends JavaPlugin {
    @Override
    public void onEnable() {
        LimeDevUtilitySpigot.init(this);
    }
}