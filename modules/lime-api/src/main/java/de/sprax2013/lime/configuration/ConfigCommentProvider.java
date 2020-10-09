package de.sprax2013.lime.configuration;

import org.jetbrains.annotations.Nullable;

// TODO: Allow to set a comment style to use
//  e.g. Normal: "# Comment"
//  e.g. Heading:
//      ###
//      # Comment
//      ###
//  e.g. Boxed:
//      ###########
//      # Comment #
//      ###########
public interface ConfigCommentProvider {
    @Nullable String getComment();
}