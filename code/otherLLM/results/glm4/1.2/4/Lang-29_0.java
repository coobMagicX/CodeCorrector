static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0.0f; // Return 0 for null or empty strings
    }
    int[] javaVersionArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    return toVersionInt(javaVersionArray);
}

// Additional method that should handle null input and throw appropriate exceptions or return default values as needed.
static int[] toJavaVersionIntArray(String version, int trimSize) {
    if (version == null || version.isEmpty()) {
        return new int[0]; // Return an empty array for null or empty strings
    }
    // Assuming the rest of the code handles the conversion properly
    // ...
}

// Example implementation of toVersionInt that assumes it is meant to handle integer arrays:
static float toVersionInt(int[] versionArray) {
    if (versionArray == null || versionArray.length < 1) {
        return 0.0f; // Return 0 for empty or invalid array input
    }
    int major = versionArray[0];
    int minor = versionArray[1] > 0 ? versionArray[1] : 0;
    float result = major + ((float)minor / 100); // Example: combining major and minor versions as a single floating-point number
    return result;
}