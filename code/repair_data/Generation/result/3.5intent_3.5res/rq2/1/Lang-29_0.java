static float toJavaVersionInt(String version) {
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}

static int[] toJavaVersionIntArray(String version, int trimSize) {
    String[] parts = version.split("\\.");
    int[] intArray = new int[trimSize];
    
    int length = Math.min(parts.length, trimSize);
    for (int i = 0; i < length; i++) {
        intArray[i] = Integer.parseInt(parts[i]);
    }
    
    return intArray;
}

static float toVersionInt(int[] versionIntArray) {
    float versionInt = 0;
    
    for (int i = 0; i < versionIntArray.length; i++) {
        versionInt += (float) versionIntArray[i] / Math.pow(10, i);
    }
    
    return versionInt;
}

static final int JAVA_VERSION_TRIM_SIZE = 3;