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
public class StringEntryValidator implements EntryValidator {
    private static StringEntryValidator instance;

    /**
     * This is a private constructor used to override the default public one.
     */
    private StringEntryValidator() {
    }

    /**
     * Because every object (in theory) has {@link #toString()},
     * we only checks if it is {@code null}.
     *
     * @param value The object to check
     *
     * @return true if {@code value} is not null, false otherwise
     */
    @Override
    public boolean isValid(@Nullable Object value) {
        return value != null;
    }

    /**
     * Because {@link #isValid(Object)} is so simple, we don't need to instantiate
     * the identical {@link StringEntryValidator} all the time.
     *
     * @return The {@link StringEntryValidator} instance
     */
    public static @NotNull StringEntryValidator get() {
        if (instance == null) {
            instance = new StringEntryValidator();
        }

        return instance;
    }
}
