package de.sprax2013.lime.configuration;

import de.sprax2013.lime.configuration.legacy.LegacyKeyUpgrader;
import org.jetbrains.annotations.Nullable;

public class LegacyConfigKey {
    private final @Nullable String key;

    private final @Nullable LegacyKeyUpgrader keyUpgrader;

    public LegacyConfigKey(@Nullable String key, @Nullable LegacyKeyUpgrader keyUpgrader) {
        this.key = key;
        this.keyUpgrader = keyUpgrader;
    }

    public @Nullable String getKey() {
        return this.key;
    }

    public @Nullable LegacyKeyUpgrader getKeyUpgrader() {
        return this.keyUpgrader;
    }
}
