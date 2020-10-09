package de.sprax2013.lime.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ConfigEntry {
    private final @NotNull String key;
    private final @Nullable Object defaultValue;
    private @Nullable ConfigCommentProvider commentProvider;

    private Object value;

    protected ConfigEntry(@NotNull String key, @Nullable Object defaultValue) {
        this(key, defaultValue, null);
    }

    protected ConfigEntry(@NotNull String key, @Nullable Object defaultValue, @Nullable ConfigCommentProvider commentProvider) {
        this.key = Objects.requireNonNull(key);
        this.defaultValue = defaultValue;
        this.commentProvider = commentProvider;

        this.value = defaultValue;
    }

    public @NotNull String getKey() {
        return this.key;
    }

    public @Nullable Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Be informed, that according to the YAML 1.1 spec,
     * comments may not associated with a particular node (witch is exactly what this method is for)
     *
     * @see <a href="https://yaml.org/spec/1.1/current.html#id864687">YAML 1.1 spec</a>
     */
    public void setCommentProvider(@Nullable ConfigCommentProvider commentProvider) {
        this.commentProvider = commentProvider;
    }

    public @Nullable ConfigCommentProvider getCommentProvider() {
        return this.commentProvider;
    }

    public @Nullable String getFormattedComment() {
        String comment = this.commentProvider != null ? this.commentProvider.getComment() : null;

        if (comment != null && !comment.isEmpty()) {
            return "# " + comment.replaceAll("\n", "\n# ");
        }

        return null;
    }

    public ConfigEntry setValue(@Nullable Object value) {
        this.value = value;

        return this;
    }

    public @Nullable Object getValue() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T getValue(T type) {
        return (T) getValue();
    }

    public boolean hasValue() {
        return this.value != null;
    }

    public boolean getValueAsBoolean() {
        return this.value instanceof Boolean ?
                (boolean) this.value :
                Boolean.parseBoolean((String) this.value);
    }

    public int getValueAsInt() {
        if (this.value == null) return 0;

        return this.value instanceof Integer ?
                (int) this.value :
                Integer.parseInt((String) this.value);
    }

    public long getValueAsLong() {
        if (this.value == null) return 0;

        return this.value instanceof Long ?
                (long) this.value :
                Long.parseLong((String) this.value);
    }

    public float getValueAsFloat() {
        if (this.value == null) return 0;

        return this.value instanceof Float ?
                (float) this.value :
                Float.parseFloat((String) this.value);
    }

    public double getValueAsDouble() {
        if (this.value == null) return 0;

        return this.value instanceof Double ?
                (double) this.value :
                Double.parseDouble((String) this.value);
    }

    public @Nullable String getValueAsString() {
        return (String) this.value;
    }

    @SuppressWarnings("unchecked")
    public @Nullable List<Object> getValueAsList() {
        return (List<Object>) this.value;
    }

    @SuppressWarnings("unchecked")
    public @Nullable List<Integer> getValueAsIntList() {
        return (List<Integer>) this.value;
    }

    @SuppressWarnings("unchecked")
    public @Nullable List<String> getValueAsStringList() {
        return (List<String>) this.value;
    }
}
