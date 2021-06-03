package de.sprax2013.lime.legacy;

import org.jetbrains.annotations.Nullable;

public interface LegacyKeyUpgrader {
    @Nullable Object accept(@Nullable Object value);
}
