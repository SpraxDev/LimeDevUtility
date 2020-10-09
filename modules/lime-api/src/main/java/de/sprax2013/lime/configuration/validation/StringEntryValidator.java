package de.sprax2013.lime.configuration.validation;

public class StringEntryValidator implements EntryValidator {
    private static final StringEntryValidator instance = new StringEntryValidator();

    private StringEntryValidator() {
    }

    @Override
    public boolean isValid(Object value) {
        return value != null;
    }

    public static StringEntryValidator get() {
        return instance;
    }
}
