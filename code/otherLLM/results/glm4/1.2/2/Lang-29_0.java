static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0; // Return default value for invalid input
    }
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}