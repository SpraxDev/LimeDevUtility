package de.sprax2013.lime.utils;

import java.util.LinkedList;
import java.util.List;

public class StringUtils {
    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String trimStringToMaxLength(String s, int maxLength) {
        if (s.length() > maxLength) {
            return s.substring(0, maxLength);
        }

        return new String(s.getBytes());
    }

    //    /**
    //     * Get all values of a String array which start with a given String
    //     *
    //     * @param value String to search for
    //     * @param list  The array that should be searched in
    //     *
    //     * @return A list with all matches
    //     */
    public static List<String> getMatches(String value, List<String> list, boolean caseInsensitive) {
        List<String> result = new LinkedList<>();

        for (String str : list) {
            if (str.startsWith(value.toLowerCase())
                    || (caseInsensitive && str.toLowerCase().startsWith(value.toLowerCase()))) {
                result.add(str);
            }
        }

        return result;
    }
}