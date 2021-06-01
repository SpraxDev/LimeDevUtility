package de.sprax2013.lime.configuration.validation;

import de.sprax2013.lime.math.MathSolver;
import org.jetbrains.annotations.NotNull;

/**
 * Use {@link #get()} to grab an instance to use
 *
 * @see EntryValidator
 * @see #isValid(Object)
 * @see #get()
 */
public class MathEquationValidator implements EntryValidator {
    private static MathEquationValidator instance;

    /**
     * This is a private constructor used to override the default public one.
     */
    private MathEquationValidator() {
    }

    /**
     * Uses {@link StringEntryValidator} to make sure the value is a string
     * and then try solving it using {@link MathSolver}.
     *
     * @param value The object to check
     *
     * @return true, if the string could be parsed and it's value be calculated, false otherwise
     *
     * @see MathSolver#solve(String)
     */
    @Override
    public boolean isValid(Object value) {
        if (StringEntryValidator.get().isValid(value)) {
            try {
                MathSolver.solve(value.toString());

                return true;
            } catch (Exception ignore) {
            }
        }

        return false;
    }

    /**
     * Because {@link #isValid(Object)} is so simple, we don't need to instantiate
     * the identical {@link MathEquationValidator} all the time.
     *
     * @return The {@link MathEquationValidator} instance
     */
    public static @NotNull MathEquationValidator get() {
        if (instance == null) {
            instance = new MathEquationValidator();
        }

        return instance;
    }
}
