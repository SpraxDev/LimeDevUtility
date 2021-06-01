package de.sprax2013.lime.configuration.validation;

import de.sprax2013.lime.configuration.validation.IntEntryValidator.MathSign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @see EntryValidator
 * @see #isValid(Object)
 * @see #get()
 * @see #get(MathSign)
 */
@SuppressWarnings("unused")
public class DoubleEntryValidator implements EntryValidator {
    private final MathSign mathSign;

    public DoubleEntryValidator(@Nullable MathSign mathSign) {
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

        double parsedInt;

        if (!(value instanceof Double)) {
            try {
                parsedInt = Double.parseDouble(value.toString());
            } catch (NumberFormatException ex) {
                return false;
            }
        } else {
            parsedInt = (double) value;
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
     * Identical to calling {@link #DoubleEntryValidator(MathSign)}} with {@link MathSign#IGNORE}
     *
     * @return The {@link DoubleEntryValidator} instance
     *
     * @see #get(MathSign)
     */
    public static @NotNull DoubleEntryValidator get() {
        return new DoubleEntryValidator(MathSign.IGNORE);
    }

    /**
     * Identical to {@link #DoubleEntryValidator(MathSign)}}
     *
     * @param mathSign Configure if the given number should be checked for e.g. a positive value (null is the same as {@link MathSign#IGNORE})
     *
     * @return The {@link DoubleEntryValidator} instance
     *
     * @see #get()
     */
    public static @NotNull DoubleEntryValidator get(@Nullable MathSign mathSign) {
        return new DoubleEntryValidator(mathSign);
    }
}