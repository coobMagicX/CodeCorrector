static float toJavaVersionInt(String version) {
    String trimmed = getJavaVersionTrimmed();
    if (version != null && !version.isEmpty()) {
        return toVersionInt(toJavaVersionIntArray(version, trimmed));
    }
    return 0; // Return 0 for empty strings instead of throwing an error or returning incorrect value
}