static float toJavaVersionInt(String version) {
    if (version == null) {
        return 0;
    }
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}