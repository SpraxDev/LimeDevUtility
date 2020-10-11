package de.sprax2013.lime.configuration;

/**
 * This interface allows to easily perform actions on specific events affecting the {@link Config}.<br>
 * e.g. third-party applications can easily add their own listeners if you allow it.
 */
public interface ConfigListener {
    /**
     * This method is called, when {@link Config#loadWithException()} has been successfully executed.<br>
     * You could for example use it to update existing parameters by reading the new/current data.
     */
    void onLoad();
}