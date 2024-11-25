private static int toJavaVersionInt(String version) {
    if (version == null) {
        throw new NullPointerException("Input string cannot be null");
    }

    String trimmedVersion = getJavaVersionTrimmed();
    if (trimmedVersion == null || !trimmedVersion.startsWith(version)) {
        throw new IllegalArgumentException("Invalid Java version format: " + version);
    }

    int[] versionArray = toJavaVersionIntArray(trimmedVersion, JAVA_VERSION_TRIM_SIZE);
    return versionArray[0] * 10000000L + versionArray[1];
}