static float toJavaVersionInt(String version) {
    String trimmedVersion = getJavaVersionTrimmed();
    return trimmedVersion != null ? toVersionInt(toJavaVersionIntArray(trimmedVersion, JAVA_VERSION_TRIM_SIZE)) : 0f;
}