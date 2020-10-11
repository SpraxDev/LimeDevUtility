package de.sprax2013.lime.bungeecord;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeeLime extends Plugin {
    @Override
    public void onEnable() {
        LimeDevUtilityBungee.init(this);
    }
}
