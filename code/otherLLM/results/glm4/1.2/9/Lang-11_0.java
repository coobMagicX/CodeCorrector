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

    // Ensure 'start' and 'end' are within the valid range before assigning default values
    if (start < 0 || end > Integer.MAX_VALUE || start >= end) {
        throw new IllegalArgumentException("Invalid 'start' or 'end' parameters.");
    }
    
    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                end = Integer.MAX_VALUE; // Use a very large number, as no specific range is defined
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        }
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
        if (letters && Character.isLetter(ch)
                || numbers && Character.isDigit(ch)
                || !letters && !numbers) {
            if ((ch >= 0x0041 && ch <= 0x005A) || // Uppercase letters
                (ch >= 0x0061 && ch <= 0x007A) || // Lowercase letters
                (ch >= 0x0030 && ch <= 0x0039)) {  // Digits
            buffer[count] = ch;
        } else if ((ch >= 0xD800 && ch <= 0xDBFF) || // High Surrogate
                   (ch >= 0xDC00 && ch <= 0xDFFF)) { // Low Surrogate
            // Handle surrogate pair logic here, but ensure 'count' is not zero before processing
            if (count == 0) {
                count++;
            }
            int highSurrogate = ch;
            if ((highSurrogate >= 0xD800 && highSurrogate <= 0xDBFF)) {
                buffer[count] = (char) (55296 + random.nextInt(64));
                count--;
            } else if ((highSurrogate >= 0xDC00 && highSurrogate <= 0xDFFF)) {
                buffer[count] = (char) (56320 + random.nextInt(64));
                count--;
            }
        }
    }
    return new String(buffer);
}