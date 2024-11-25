public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0. Please consider valid range: [" + start + ", " + end + ").");
    }
    // ... rest of the method remains the same ...
}