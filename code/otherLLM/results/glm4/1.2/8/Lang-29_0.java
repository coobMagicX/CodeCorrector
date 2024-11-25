static float toJavaVersionInt(String version) {
    if (version == null || version.trim().isEmpty()) {
        return 0f; // Handle null or empty string input
    }
    int[] javaVersions = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    return toVersionInt(javaVersions);
}