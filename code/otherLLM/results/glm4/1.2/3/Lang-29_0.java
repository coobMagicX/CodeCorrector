static float toJavaVersionInt(String version) {
    // Check for null or empty string and return 0 as the default value
    if (version == null || version.isEmpty()) {
        return 0f;
    }

    // Use the provided method to convert the Java version to an integer array
    int[] javaVersionArray = toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE);
    
    // Since the return type is float and we need to ensure it's not a double cast,
    // convert from int array to float only if the conversion makes sense (i.e., all integers are non-negative)
    boolean isValidConversion = true;
    for (int i : javaVersionArray) {
        if (i < 0) {
            isValidConversion = false;
            break;
        }
    }

    // If conversion is valid, return the average as float. Otherwise, return 0.
    if (isValidConversion && javaVersionArray.length > 0) {
        int sum = 0;
        for (int i : javaVersionArray) {
            sum += i;
        }
        return (float) sum / javaVersionArray.length;
    } else {
        return 0f;
    }
}