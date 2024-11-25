static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0; // Return a default value for null or empty input
    }
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}