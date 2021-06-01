package de.sprax2013.lime.utils;

public class UUIDUtils {
    private UUIDUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String addHyphensToUUID(String uuidStr) {
        return uuidStr.replace("-", "")
                .replaceFirst("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5");
    }
}