package de.sprax2013.lime.configuration;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ConfigSerializable {
    /**
     * I recommend to use {@link java.util.LinkedHashMap}, so you can make sure
     * that your order of insertion is represented in the resulting {@link Config}
     * <br>
     * <br>
     * The values of the returned Map may contain more Lists, Maps, etc.
     * as the given values are serialized recursively
     *
     * @return A Map containing Key-&gt;Value pairs that can be serialized to a {@link String}
     */
    @Nullable
    Map<String, Object> serializeToMap();
}