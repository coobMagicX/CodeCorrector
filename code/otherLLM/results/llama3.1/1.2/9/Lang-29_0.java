static int toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0; // return 0 for empty or null input
    }

    String trimmedVersion = getJavaVersionTrimmed();
    if (trimmedVersion == null) {
        return 0; // return 0 for invalid input
    }

    int[] versionIntArray = toJavaVersionIntArray(trimmedVersion, JAVA_VERSION_TRIM_SIZE);
    return toVersionInt(versionIntArray);
}