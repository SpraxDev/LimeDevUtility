package de.sprax2013.lime;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides static variables and methods that are used by LimeDevUtility
 */
public class LimeDevUtility {
    /**
     * The {@link Logger} used by LimeDevUtility for outputting to the console
     *
     * @see Logger#setLevel(Level)
     */
    public static final Logger LOGGER = Logger.getLogger("LimeDevUtility");

    /**
     * This is a private constructor used to override the default public one.<br>
     * This class is not intended to be instantiated.
     */
    private LimeDevUtility() {
        throw new IllegalStateException("Utility class");
    }
}
