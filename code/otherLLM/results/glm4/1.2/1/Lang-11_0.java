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

    if (start > end || (start < 0 && end >= 0)) {
        throw new IllegalArgumentException("Start parameter (" + start + ") is greater than or equal to end parameter (" + end + ").");
    }
    
    // If start and end are both zero, determine the range based on available characters
    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                end = Integer.MAX_VALUE;
            } else {
                // Assuming ASCII range for letters and numbers
                end = 'z';
                start = ' ';
            }
        }
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            // Use the range from start to 'end' as defined in this context
            ch = (char) ((random.nextInt(gap) + start) % 65536);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        
        if (!Character.isLetterOrDigit(ch)) {
            // If not a letter or digit, ensure it's still within the range
            if (ch < start || ch > end) {
                throw new IllegalArgumentException("The character " + ch + " is out of range [" + start + ", " + end + "].");
            }
        } else {
            // Check for surrogates
            if ((ch >= 0xD800 && ch <= 0xDBFF) || (ch >= 0xDC00 && ch <= 0xDFFF)) {
                throw new IllegalArgumentException("Surrogate characters are not allowed.");
            }
        }

        buffer[count] = ch;
    }
    return new String(buffer);
}