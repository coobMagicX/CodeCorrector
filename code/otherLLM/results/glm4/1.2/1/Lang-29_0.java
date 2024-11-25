static int toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0;
    }
    try {
        return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
    } catch (Exception e) {
        // Handle any exception that might occur during the conversion
        return 0;
    }
}