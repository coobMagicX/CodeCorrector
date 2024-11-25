static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0.0f; // Return a default value for invalid input
    }

    int[] javaVersionIntArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    return toVersionFloat(javaVersionIntArray); // Use the existing method
}