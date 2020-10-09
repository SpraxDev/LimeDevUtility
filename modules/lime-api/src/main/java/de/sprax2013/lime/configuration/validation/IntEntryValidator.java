package de.sprax2013.lime.configuration.validation;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class IntEntryValidator implements EntryValidator {
    private static IntEntryValidator instance;

    private IntEntryValidator() {
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) return false;
        if (value instanceof Integer) return true;

        try {
            Integer.parseInt(value.toString());

            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static @NotNull IntEntryValidator get() {
        if (instance == null) {
            instance = new IntEntryValidator();
        }

        return instance;
    }
}
