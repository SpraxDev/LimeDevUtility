package de.sprax2013.lime.configuration.validation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @see EntryValidator
 * @see #isValid(Object)
 * @see #get()
 * @see #get(MathSign)
 */
@SuppressWarnings("unused")
public class IntEntryValidator implements EntryValidator {
    private final MathSign mathSign;

    public IntEntryValidator(@Nullable MathSign mathSign) {
        this.mathSign = mathSign == null ? MathSign.IGNORE : mathSign;
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
        if (value == null) return false;

        int parsedInt;

        if (!(value instanceof Integer)) {
            try {
                parsedInt = Integer.parseInt(value.toString());
            } catch (NumberFormatException ex) {
                return false;
            }
        } else {
            parsedInt = (int) value;
        }

        switch (this.mathSign) {
            case IGNORE:
                return true;
            case POSITIVE:
                return parsedInt >= 0;
            case POSITIVE_IGNORE_ZERO:
                return parsedInt > 0;
            case NEGATIVE:
                return parsedInt <= 0;
            case NEGATIVE_IGNORE_ZERO:
                return parsedInt < 0;
            default:
                throw new RuntimeException("Could not process MathSign: " + this.mathSign);
        }
    }

    /**
     * Identical to calling {@link #IntEntryValidator(MathSign)}} with {@link MathSign#IGNORE}
     *
     * @return The {@link IntEntryValidator} instance
     *
     * @see #get(MathSign)
     */
    public static @NotNull IntEntryValidator get() {
        return new IntEntryValidator(MathSign.IGNORE);
    }

    /**
     * Identical to {@link #IntEntryValidator(MathSign)}}
     *
     * @param mathSign Configure if the given number should be checked for e.g. a positive value (null is the same as {@link MathSign#IGNORE})
     *
     * @return The {@link IntEntryValidator} instance
     *
     * @see #get()
     */
    public static @NotNull IntEntryValidator get(@Nullable MathSign mathSign) {
        return new IntEntryValidator(mathSign);
    }

    public enum MathSign {
        IGNORE, POSITIVE, POSITIVE_IGNORE_ZERO, NEGATIVE, NEGATIVE_IGNORE_ZERO
    }
}