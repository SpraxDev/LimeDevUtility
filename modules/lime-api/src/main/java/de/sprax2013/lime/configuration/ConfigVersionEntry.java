package de.sprax2013.lime.configuration;

import de.sprax2013.lime.configuration.validation.IntEntryValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigVersionEntry extends ConfigEntry {
    private final int targetVersion;

    protected ConfigVersionEntry(@NotNull String key, int cfgVersion, int firstVersion, @Nullable ConfigCommentProvider commentProvider) {
        super(key, firstVersion, commentProvider);

        if (firstVersion > cfgVersion) {
            throw new IllegalArgumentException("First version is greater than the current version (" + firstVersion + " > " + cfgVersion + ")");
        }

        this.targetVersion = cfgVersion;

        super.setEntryValidator(IntEntryValidator.get());
    }

    public int getTargetVersion() {
        return this.targetVersion;
    }

    public int getCurrentVersion() {
        return super.getValueAsInt();
    }

    void incrementVersion() {
        super.setValue(getCurrentVersion() + 1);
    }

    public boolean requiresUpgrade() {
        return getCurrentVersion() < getTargetVersion();
    }
}
