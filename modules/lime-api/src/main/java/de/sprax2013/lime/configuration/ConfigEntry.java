package de.sprax2013.lime.configuration;

import de.sprax2013.lime.configuration.validation.EntryValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A {@link ConfigEntry} consist of an {@code key}, a given {@code defaultValue} and the current {@code value}
 * read from the file or set by the developer.<br><br>
 * You can additionally assign an {@link EntryValidator} and {@link ConfigCommentProvider}.
 *
 * @see Config#createEntry(String, Object)
 * @see Config#withEntry(String, Object)
 * @see Config#addCommentEntry(String, String)
 * @see Config#withCommentEntry(String, String)
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ConfigEntry {
    private final @NotNull String key;
    private final @Nullable Object defaultValue;

    private @Nullable EntryValidator entryValidator;
    private @Nullable ConfigCommentProvider commentProvider;

    private Object value;

    /**
     * @param key          An unique identifier within the {@link Config}
     * @param defaultValue The default value for this entry
     *
     * @see EntryValidator
     */
    protected ConfigEntry(@NotNull String key, @Nullable Object defaultValue) {
        this(key, defaultValue, null);
    }

    /**
     * @param key             An unique identifier within the {@link Config}
     * @param defaultValue    The default value for this entry
     * @param commentProvider A {@link ConfigCommentProvider} that generates a comment or null
     *
     * @see EntryValidator
     * @see ConfigCommentProvider
     */
    protected ConfigEntry(@NotNull String key, @Nullable Object defaultValue, @Nullable ConfigCommentProvider commentProvider) {
        this.key = Objects.requireNonNull(key);
        this.defaultValue = defaultValue;
        this.commentProvider = commentProvider;

        this.value = defaultValue;
    }

    /**
     * The key is unique within a {@link Config}, thus there can be same keys in differen {@link Config}s.<br><br>
     * Every {@code .} inside the {@code key} causes one indentation further down the tree.<br><br>
     * <p>
     * e.g. {@code "Root.Profile.Username"} turns into the following YAML structure:<br>
     * {@code Root:}<br>
     * &nbsp;&nbsp;{@code Profile:}<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;{@code Username: [Value]}
     *
     * @return The unique identifier that has been assigned to this {@link ConfigEntry}
     */
    public @NotNull String getKey() {
        return this.key;
    }

    /**
     * @return The default value that has been provided via the constructor
     *
     * @see #ConfigEntry(String, Object)
     * @see #ConfigEntry(String, Object, ConfigCommentProvider)
     */
    public @Nullable Object getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @param entryValidator The {@link EntryValidator} to use, or {@code null}
     *
     * @return The same {@link ConfigEntry} for chaining
     *
     * @see EntryValidator
     * @see #getEntryValidator()
     */
    public @NotNull ConfigEntry setEntryValidator(@Nullable EntryValidator entryValidator) {
        if (entryValidator != null && !entryValidator.isValid(this.defaultValue))
            throw new IllegalArgumentException("The EntryValidator returns false for the Entry's default value");

        this.entryValidator = entryValidator;

        return this;
    }

    /**
     * @return The {@link EntryValidator} assigned, or {@code null}
     *
     * @see EntryValidator
     * @see #setEntryValidator(EntryValidator)
     */
    public @Nullable EntryValidator getEntryValidator() {
        return this.entryValidator;
    }

    /**
     * <em>Be informed, that according to the YAML 1.1 spec,
     * comments may not associated with a particular node (witch is exactly what this method is for)</em>
     *
     * @param commentProvider The {@link ConfigCommentProvider} to use, or {@code null}
     *
     * @return The same {@link ConfigEntry} for chaining
     *
     * @see <a href="https://yaml.org/spec/1.1/current.html#id864687">YAML 1.1 spec</a>
     * @see ConfigCommentProvider
     * @see #getCommentProvider()
     */
    public @NotNull ConfigEntry setCommentProvider(@Nullable ConfigCommentProvider commentProvider) {
        this.commentProvider = commentProvider;

        return this;
    }

    /**
     * @return The {@link ConfigCommentProvider} assigned, or {@code null}
     *
     * @see ConfigCommentProvider
     * @see #setCommentProvider(ConfigCommentProvider)
     */
    public @Nullable ConfigCommentProvider getCommentProvider() {
        return this.commentProvider;
    }

    /**
     * This method adds the {@code #} to the beginning of the lines, to tell the YAML parser to ignore these lines<br>
     * This method might apply more complex structures according to the defined {@link CommentStyle}.
     *
     * @return Formatted comment {@link String}, or {@code null}
     *
     * @see CommentStyle
     * @see #setCommentProvider(ConfigCommentProvider)
     * @see #getCommentProvider()
     */
    public @Nullable String getFormattedComment() {
        String comment = this.commentProvider != null ? this.commentProvider.getComment() : null;

        if (comment != null && !comment.isEmpty()) {
            return "# " + comment.replaceAll("\n", "\n# ");
        }

        return null;
    }

    /**
     * Sets the {@code value}.<br>
     * <strong>Arrays are automatically converted into a {@link List}!</strong>
     *
     * @param value The value to set the entry to
     *
     * @return The same {@link ConfigEntry} for chaining
     *
     * @see #getValue()
     * @see #getDefaultValue()
     */
    public @NotNull ConfigEntry setValue(@Nullable Object value) {
        this.value = value instanceof Object[] ? Arrays.asList((Object[]) value) : value;

        return this;
    }

    /**
     * @return Returns the current value as is (can be {@code null})
     *
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue(Object)
     * @see #getValueAsBoolean()
     * @see #getValueAsDouble()
     * @see #getValueAsFloat()
     * @see #getValueAsInt()
     * @see #getValueAsLong()
     * @see #getValueAsString()
     * @see #getValueAsList()
     * @see #getValueAsStringList()
     */
    public @Nullable Object getValue() {
        return this.value;
    }

    /**
     * @param type The type, the value should be casted to
     * @param <T>  The type, the value should be casted to
     *
     * @return Returns the current value casted to the given type (can be {@code null})
     *
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValueAsBoolean()
     * @see #getValueAsDouble()
     * @see #getValueAsFloat()
     * @see #getValueAsInt()
     * @see #getValueAsLong()
     * @see #getValueAsString()
     * @see #getValueAsList()
     * @see #getValueAsStringList()
     */
    @SuppressWarnings("unchecked")
    public @Nullable <T> T getValue(T type) {
        return (T) this.value;
    }

    /**
     * @return True if {@link #getValue()} {@code != null}, false otherwise
     */
    public boolean hasValue() {
        return this.value != null;
    }

    public <T extends Enum<T>> T getValueAsEnum(Class<T> enumType) {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result instanceof String) {
            return Enum.valueOf(enumType, (String) result);
        }

        if (enumType.isInstance(result)) {
            return enumType.cast(result);
        }

        return null;
    }

    /**
     * If {@link EntryValidator} is not {@code null} and {@link EntryValidator#isValid(Object)} is {@code false},
     * the {@code defaultValue} will be used instead.<br>
     * <p>
     * One of these two values is then casted to a {@code boolean} (if it is an {@code boolean})
     * or it is casted to a {@link String} and fed into {@link Boolean#parseBoolean(String)}
     *
     * @return Returns one of the values as a {@code boolean}
     *
     * @see Boolean#parseBoolean(String)
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValue(Object)
     * @see #getValueAsDouble()
     * @see #getValueAsFloat()
     * @see #getValueAsInt()
     * @see #getValueAsLong()
     * @see #getValueAsString()
     * @see #getValueAsList()
     * @see #getValueAsStringList()
     */
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

    /**
     * If {@link EntryValidator} is not {@code null} and {@link EntryValidator#isValid(Object)} is {@code false},
     * the {@code defaultValue} will be used instead.<br>
     * <p>
     * One of these two values is then casted to a {@code int} (if it is an {@code int})
     * or it is casted to a {@link String} and fed into {@link Integer#parseInt(String)}
     *
     * @return Returns one of the value as an {@code int}
     *
     * @see Integer#parseInt(String)
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValue(Object)
     * @see #getValueAsBoolean()
     * @see #getValueAsDouble()
     * @see #getValueAsFloat()
     * @see #getValueAsLong()
     * @see #getValueAsString()
     * @see #getValueAsList()
     * @see #getValueAsStringList()
     */
    public int getValueAsInt() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result == null) return 0;

        return result instanceof Number ?
                ((Number) result).intValue() :
                Integer.parseInt((String) result);
    }

    /**
     * If {@link EntryValidator} is not {@code null} and {@link EntryValidator#isValid(Object)} is {@code false},
     * the {@code defaultValue} will be used instead.<br>
     * <p>
     * One of these two values is then casted to a {@code long} (if it is an {@code long})
     * or it is casted to a {@link String} and fed into {@link Long#parseLong(String)}
     *
     * @return Returns one of the value as an {@code long}
     *
     * @see Long#parseLong(String)
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValue(Object)
     * @see #getValueAsBoolean()
     * @see #getValueAsDouble()
     * @see #getValueAsFloat()
     * @see #getValueAsInt()
     * @see #getValueAsString()
     * @see #getValueAsList()
     * @see #getValueAsStringList()
     */
    public long getValueAsLong() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result == null) return 0;

        return result instanceof Number ?
                ((Number) result).longValue() :
                Long.parseLong((String) result);
    }

    /**
     * If {@link EntryValidator} is not {@code null} and {@link EntryValidator#isValid(Object)} is {@code false},
     * the {@code defaultValue} will be used instead.<br>
     * One of these two values is then casted to a {@code float} (if it is an {@code float})
     * or it is casted to a {@link String} and fed into {@link Float#parseFloat(String)}
     *
     * @return Returns one of the value as an {@code float}
     *
     * @see Float#parseFloat(String)
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValue(Object)
     * @see #getValueAsBoolean()
     * @see #getValueAsDouble()
     * @see #getValueAsInt()
     * @see #getValueAsLong()
     * @see #getValueAsString()
     * @see #getValueAsList()
     * @see #getValueAsStringList()
     */
    public float getValueAsFloat() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result == null) return 0;

        return result instanceof Number ?
                ((Number) result).floatValue() :
                Float.parseFloat((String) result);
    }

    /**
     * If {@link EntryValidator} is not {@code null} and {@link EntryValidator#isValid(Object)} is {@code false},
     * the {@code defaultValue} will be used instead.<br>
     * One of these two values is then casted to a {@code double} (if it is an {@code double})
     * or it is casted to a {@link String} and fed into {@link Double#parseDouble(String)}
     *
     * @return Returns one of the value as an {@code double}
     *
     * @see Double#parseDouble(String)
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValue(Object)
     * @see #getValueAsBoolean()
     * @see #getValueAsFloat()
     * @see #getValueAsInt()
     * @see #getValueAsLong()
     * @see #getValueAsString()
     * @see #getValueAsList()
     * @see #getValueAsStringList()
     */
    public double getValueAsDouble() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        if (result == null) return 0;

        return result instanceof Number ?
                ((Number) result).doubleValue() :
                Double.parseDouble((String) result);
    }

    /**
     * If {@link EntryValidator} is not {@code null} and {@link EntryValidator#isValid(Object)} is {@code false},
     * the {@code defaultValue} will be used instead.<br>
     * One of these two values is then checked whether it is {@code null},
     * if not {@link Object#toString()} is returned
     *
     * @return Returns one of the values {@link Object#toString()} output or {@code null}
     *
     * @see Object#toString()
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValue(Object)
     * @see #getValueAsBoolean()
     * @see #getValueAsDouble()
     * @see #getValueAsFloat()
     * @see #getValueAsInt()
     * @see #getValueAsLong()
     * @see #getValueAsList()
     * @see #getValueAsStringList()
     */
    public @Nullable String getValueAsString() {
        Object result;

        if (entryValidator == null || entryValidator.isValid(this.value)) {
            result = this.value;
        } else {
            result = this.defaultValue;
        }

        return result == null ? null : result.toString();
    }

    /**
     * If {@link EntryValidator} is not {@code null} and {@link EntryValidator#isValid(Object)} is {@code false},
     * the {@code defaultValue} will be used instead.<br>
     * One of these two values is then casted to an {@link List} with {@link Object}s
     *
     * @return Returns one of the values as {@link List} or {@code null}
     *
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValue(Object)
     * @see #getValueAsBoolean()
     * @see #getValueAsDouble()
     * @see #getValueAsFloat()
     * @see #getValueAsInt()
     * @see #getValueAsLong()
     * @see #getValueAsString()
     * @see #getValueAsStringList()
     */
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

    /**
     * If {@link EntryValidator} is not {@code null} and {@link EntryValidator#isValid(Object)} is {@code false},
     * the {@code defaultValue} will be used instead.<br>
     * One of these two values is then casted to an {@link List}
     *
     * @return Returns one of the values as {@link List} with {@link String}s or {@code null}
     *
     * @see Object#toString()
     * @see #getDefaultValue()
     * @see #setValue(Object)
     * @see #getValue()
     * @see #getValue(Object)
     * @see #getValueAsBoolean()
     * @see #getValueAsDouble()
     * @see #getValueAsFloat()
     * @see #getValueAsInt()
     * @see #getValueAsLong()
     * @see #getValueAsString()
     * @see #getValueAsList()
     */
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