package de.sprax2013.lime.spigot.minecraft;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

// TODO Automatically cache in-memory
// TODO Allow to disable SpraxAPI calls and only use Mojang-API
// TODO Allow setting own API-URL and Fallback-URLs (default is SpraxAPI and Mojang as Fallback)
//      Is the main API offline or sending status 429, is the Fallback used instead for the next couple of seconds.
//      Are both down are both put into a grace period that can be configured (default is 10 seconds)
// TODO Add #getGameProfile() to profile/user data that generates a new GameProfile on each call
public class MinecraftAPI {
    private final String userAgent;

    public MinecraftAPI(String userAgent) {
        this.userAgent = userAgent;
    }

    public MinecraftAPI(JavaPlugin plugin) {
        this(plugin.getDescription().getName() + "/" + plugin.getDescription().getVersion() +
                " (" + System.getProperty("os.name") + "; " + System.getProperty("os.arch") +
                "; Java " + System.getProperty("java.version") + ")");
    }

    public Object getUniqueId(@NotNull String username, long at) {
        throw new NotImplementedException();

        // https://api.sprax2013.de/mc/uuid/:username?at=:time
        // https://api.mojang.com/users/profiles/minecraft/:username?at=:time
    }

    public Object getUniqueId(@NotNull String username) {
        return getUniqueId(username, -1);
    }

    public Object getProfile(String nameOrId) {
        throw new NotImplementedException();

        // https://api.sprax2013.de/mc/profile/:userOrId?full=true
        // https://sessionserver.mojang.com/session/minecraft/profile/:uuid?unsigned=false
    }

    public Object getProfile(UUID uuid) {
        return getProfile(uuid.toString());
    }

    public Object getNameHistory(UUID uuid) {
        throw new NotImplementedException();

        // https://api.sprax2013.de/mc/history/:uuid
    }

    public Object getUser(String nameOrId) {
        throw new NotImplementedException();

        // TODO Request https://api.sprax2013.de/mc/profile/:userOrId?full=true or construct manually from Mojang API calls
    }

    public Object getBlockedServers() {
        throw new NotImplementedException();
    }

    public Object isServerBlocked(String host) {
        throw new NotImplementedException();
    }
}