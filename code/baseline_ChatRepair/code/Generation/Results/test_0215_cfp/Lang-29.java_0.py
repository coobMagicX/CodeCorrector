static int toJavaVersionInt(String version) {
    return (int) toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}
