static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0;
    }
    try {
        return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
    } catch (NumberFormatException e) {
        return 0;
    }
}