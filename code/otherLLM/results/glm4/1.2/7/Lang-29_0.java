static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        // Return 0 for null or empty strings as per the intent of handling invalid inputs
        return 0;
    }
    
    try {
        int[] javaVersionArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
        return toVersionInt(javaVersionArray);
    } catch (IllegalArgumentException e) {
        // Handle cases where the version string is not in a valid format
        return 0;
    }
}