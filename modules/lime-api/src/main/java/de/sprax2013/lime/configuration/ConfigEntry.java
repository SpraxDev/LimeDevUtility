package de.sprax2013.lime.configuration;

import de.sprax2013.lime.configuration.validation.EntryValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ConfigEntry {
    private final @NotNull String key;
    private final @Nullable Object defaultValue;

    private @Nullable EntryValidator entryValidator;
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

    public @NotNull ConfigEntry setEntryValidator(@Nullable EntryValidator entryValidator) {
        if (entryValidator != null && !entryValidator.isValid(this.defaultValue))
            throw new IllegalArgumentException("The EntryValidator returns false for the Entry's default value");

        this.entryValidator = entryValidator;

        return this;
    }

    public @Nullable EntryValidator getEntryValidator() {
        return this.entryValidator;
    }

    /**
     * Be informed, that according to the YAML 1.1 spec,
     * comments may not associated with a particular node (witch is exactly what this method is for)
     *
     * @see <a href="https://yaml.org/spec/1.1/current.html#id864687">YAML 1.1 spec</a>
     */
    public @NotNull ConfigEntry setCommentProvider(@Nullable ConfigCommentProvider commentProvider) {
        this.commentProvider = commentProvider;

        return this;
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

    public @NotNull ConfigEntry setValue(@Nullable Object value) {
        this.value = value instanceof Object[] ? Arrays.asList((Object[]) value) : value;

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
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        return result instanceof Boolean ?
                (boolean) result :
                Boolean.parseBoolean((String) result);
    }

    public int getValueAsInt() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result == null) return 0;

        return result instanceof Integer ?
                (int) result :
                Integer.parseInt((String) result);
    }

    public long getValueAsLong() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result == null) return 0;

        return result instanceof Long ?
                (long) result :
                Long.parseLong((String) result);
    }

    public float getValueAsFloat() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result == null) return 0;

        return result instanceof Float ?
                (float) result :
                Float.parseFloat((String) result);
    }

    public double getValueAsDouble() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result == null) return 0;

        return result instanceof Double ?
                (double) result :
                Double.parseDouble((String) result);
    }

    public @Nullable String getValueAsString() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        return result == null ? null : result.toString();
    }

    @SuppressWarnings("unchecked")
    public @Nullable List<Object> getValueAsList() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        return (List<Object>) result;
    }

    @SuppressWarnings("unchecked")
    public @Nullable List<Integer> getValueAsIntList() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        return (List<Integer>) result;
    }

    @SuppressWarnings("unchecked")
    public @Nullable List<String> getValueAsStringList() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        return (List<String>) result;
    }
}
