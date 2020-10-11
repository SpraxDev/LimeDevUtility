package de.sprax2013.lime.configuration.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Use {@link #get()} to grab an instance to use
 *
 * @see EntryValidator
 * @see #isValid(Object)
 * @see #get()
 */
@SuppressWarnings("unused")
public class IntEntryValidator implements EntryValidator {
    private static IntEntryValidator instance;

    /**
     * This is a private constructor used to override the default public one.
     */
    private IntEntryValidator() {
    }

    /**
     * This method checks if {@code value} is an instance of {@link Integer} or can be parsed as one.
     *
     * @param value The object to check
     *
     * @return true if {@code value} is a valid {@link Integer}, false otherwise
     *
     * @see Integer#parseInt(String)
     */
    @Override
    public boolean isValid(@Nullable Object value) {
        if (value == null) return false;
        if (value instanceof Integer) return true;

        try {
            Integer.parseInt(value.toString());

            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Because {@link #isValid(Object)} is so simple, we don't need to instantiate
     * the identical {@link IntEntryValidator} all the time.<br>
     * That's why you should use this method to grab an instance to use.
     *
     * @return The {@link IntEntryValidator} instance
     */
    public static @NotNull IntEntryValidator get() {
        if (instance == null) {
            instance = new IntEntryValidator();
        }

        return instance;
    }
}
