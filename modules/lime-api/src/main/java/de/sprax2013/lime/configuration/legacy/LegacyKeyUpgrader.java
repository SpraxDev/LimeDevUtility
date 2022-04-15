package de.sprax2013.lime.configuration.legacy;

import org.jetbrains.annotations.Nullable;

public interface LegacyKeyUpgrader {
    @Nullable Object accept(@Nullable Object value);
}
