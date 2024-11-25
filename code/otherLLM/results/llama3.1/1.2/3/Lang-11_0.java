public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    
    // Input validation for start and end
    if (start > end) {
        throw new IllegalArgumentException("Start value (" + start + ") cannot be greater than end value (" + end + ").");
    }

    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty");
    }
    
    // Rest of the code remains the same...
}