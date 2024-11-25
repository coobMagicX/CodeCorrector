static float toJavaVersionInt(String version) {
    if (version == null) {
        throw new NullPointerException("The version string cannot be null");
    }
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}