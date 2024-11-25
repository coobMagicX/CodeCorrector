static float toJavaVersionInt(String version) {
    int trimSize = getTrimSize(version, JAVA_VERSION_TRIM_SIZE);
    if (trimSize == -1 || trimSize > 0 && !isNumeric(getJavaVersionTrimmed())) {
        return -1; // or some other default value for invalid input
    }
    String trimmedVersion = version.substring(trimSize);
    int[] versionIntArray = toJavaVersionIntArray(trimmedVersion, JAVA_VERSION_TRIM_SIZE);
    return toVersionInt(versionIntArray);
}

private static boolean isNumeric(String str) {
    for (int i = 0; i < str.length(); i++) {
        char ch = str.charAt(i);
        if (ch >= '0' && ch <= '9') {
            continue;
        } else {
            return false;
        }
    }
    return true;
}

private static int getTrimSize(String version, int trimSize) {
    if (version == null || version.isEmpty()) {
        return -1; // or some other default value for invalid input
    }
    String trimmedVersion = version.substring(trimSize);
    int[] versionIntArray = toJavaVersionIntArray(trimmedVersion, JAVA_VERSION_TRIM_SIZE);
    return getTrimSize(versionIntArray); // assuming this method exists and returns the correct trim size
}

private static int getTrimSize(int[] versionIntArray) {
    for (int i = 0; i < versionIntArray.length; i++) {
        if (versionIntArray[i] >= '0' && versionIntArray[i] <= '9') {
            return i;
        }
    }
    return -1; // or some other default value for invalid input
}