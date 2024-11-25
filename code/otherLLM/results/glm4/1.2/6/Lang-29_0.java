static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        throw new IllegalArgumentException("Version string cannot be null or empty");
    }
    
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}