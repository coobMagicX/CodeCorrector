public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty");
    }

    // Ensure start and end are within the correct range
    if (start < 0 || end <= 0) {
        throw new IllegalArgumentException("Start must be non-negative and end must be positive, but got start=" + start + " and end=" + end);
    }
    
    // If chars array is provided, use it; otherwise, determine the range dynamically
    if (chars == null) {
        if (!letters && !numbers) {
            throw new IllegalArgumentException("Both letters and numbers must be true when no character array is provided");
        } else {
            if (letters) {
                end = 'z';
                start = ' ';
            }
            if (numbers) {
                end = '9';
                start = '0';
            }
        }
    }

    // If both start and end are 0, we should have a defined range already
    if (start == 0 && end == 0) {
        // previous code has already handled the default range assignment
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        // The rest of the code for handling letters, numbers and surrogates remains unchanged
    }
    return new String(buffer);
}