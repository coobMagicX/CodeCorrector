static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        throw new IllegalArgumentException("Invalid Java version string");
    }
    String trimmedVersion = getJavaVersionTrimmed(version);
    if (trimmedVersion != null) {
        return toVersionInt(toJavaVersionIntArray(trimmedVersion, JAVA_VERSION_TRIM_SIZE));
    } else {
        // Handle non-standard version formats or "XXX-1.3.x" cases
        // For this example, we'll assume a default behavior of returning 0 as the integer representation
        return 0;
    }
}