static float toJavaVersionInt(String version) {
    return toVersionInt(toJavaVersionIntArray(version.trim(), JAVA_VERSION_TRIM_SIZE));
}