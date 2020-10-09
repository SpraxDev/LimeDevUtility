package de.sprax2013.lime.configuration.validation;

@SuppressWarnings("unused")
public class StringEntryValidator implements EntryValidator {
    private static StringEntryValidator instance;

    private StringEntryValidator() {
    }

    @Override
    public boolean isValid(Object value) {
        return value != null;
    }

    public static StringEntryValidator get() {
        if (instance == null) {
            instance = new StringEntryValidator();
        }

        return instance;
    }
}
