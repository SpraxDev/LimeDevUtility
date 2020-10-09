package de.sprax2013.lime.configuration.validation;

public class IntEntryValidator implements EntryValidator {
    private static final IntEntryValidator instance = new IntEntryValidator();

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

    public static IntEntryValidator get() {
        return instance;
    }
}
