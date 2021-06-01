package de.sprax2013.lime.configuration.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @see EntryValidator
 * @see #isValid(Object)
 * @see #get(Class)
 */
@SuppressWarnings("unused")
public class EnumEntryValidator implements EntryValidator {
    @SuppressWarnings("rawtypes")
    private final Class<? extends Enum> enumType;

    @SuppressWarnings("rawtypes")
    public EnumEntryValidator(@NotNull Class<? extends Enum> enumType) {
        if (!enumType.isEnum()) throw new IllegalArgumentException("The given 'enumType' is not an enum");

        this.enumType = Objects.requireNonNull(enumType);
    }

    /**
     * Because every object (in theory) has {@link #toString()},
     * we only checks if it is {@code null}.
     *
     * @param value The object to check
     *
     * @return true if {@code value} is not null, false otherwise
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isValid(@Nullable Object value) {
        if (value instanceof String) {
            try {
                Enum.valueOf(enumType, (String) value);

                return true;
            } catch (IllegalArgumentException ignore) {
            }
        }

        return enumType.isInstance(value);
    }

    /**
     * Identical to {@link #EnumEntryValidator(Class)}
     *
     * @param enumType The allowed enum class
     *
     * @return The {@link EnumEntryValidator} instance
     */
    @SuppressWarnings("rawtypes")
    public static @NotNull EnumEntryValidator get(@NotNull Class<? extends Enum> enumType) {
        return new EnumEntryValidator(enumType);
    }
}