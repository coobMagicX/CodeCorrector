static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return -1; // Return a specific value for invalid inputs
    }
    
    String trimmedVersion = getJavaVersionTrimmed();
    if (trimmedVersion != null) {
        int[] versionArray = toJavaVersionIntArray(trimmedVersion, JAVA_VERSION_TRIM_SIZE);
        return toVersionInt(versionArray); // Correctly convert the array to an integer
    } else {
        return -1; // Return a specific value for invalid inputs
    }
}