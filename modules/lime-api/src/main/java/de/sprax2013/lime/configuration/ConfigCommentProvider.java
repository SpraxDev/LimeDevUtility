package de.sprax2013.lime.configuration;

import org.jetbrains.annotations.Nullable;

/**
 * This interface allows you to provide a method to generate a comment for an {@link ConfigEntry}.<br>
 * This allows you to set, before the used variables/methods are available/ready to be used.<br>
 * It makes generating dynamic comments easier.
 */
public interface ConfigCommentProvider {
    /**
     * This method should provide a {@link String} representing the comment.<br>
     * Escape characters(={@code #}) (for comments) are added afterwards
     *
     * @return The comment string (without {@code #}) or {@code null} for no comment
     */
    @Nullable String getComment();
}