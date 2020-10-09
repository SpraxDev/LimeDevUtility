package de.sprax2013.lime.configuration.validation;

/**
 * Using an {@link EntryValidator}, allows you to automatically fallback to the
 * default value you have given your {@link de.sprax2013.lime.configuration.ConfigEntry} instead of
 * verifying it yourself for every entry.<br><br>
 * Writing your own, allows you to reuse it for all
 * your {@link de.sprax2013.lime.configuration.ConfigEntry}s, if applicable
 */
public interface EntryValidator {
    /**
     * Depending on the implementation, this method should check the given object
     * for its type and optionally its content to decide whether it is valid or not.
     *
     * @param value The object to check
     *
     * @return True, if {@code value} is of the right type and optionally the right content, false otherwise
     */
    boolean isValid(Object value);
}
