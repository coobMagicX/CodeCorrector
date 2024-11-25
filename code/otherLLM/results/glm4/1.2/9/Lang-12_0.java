public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    
    // Ensure valid start and end range
    if (start > end) {
        throw new IllegalArgumentException("Start index " + start + " cannot be greater than end index " + end + ".");
    }
    
    // Adjust the end value for character ranges
    if ((letters || numbers) && (start == 0 && end == 0)) {
        if (!letters && !numbers) {
            end = Integer.MAX_VALUE;
        } else {
            if (letters && Character.isLetter('z')) {
                end = 'z' + 1;
            }
            if (numbers && Character.isDigit('9')) {
                end = '9' + 1;
            }
            start = ' '; // Default to space if no specific range is provided
        }
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
        char ch;
        if (chars == null || chars.length == 0) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        
        // Check for valid character according to the flags
        boolean isValidChar = true;
        if (letters && !Character.isLetter(ch)) {
            isValidChar = false;
        }
        if (numbers && !Character.isDigit(ch)) {
            isValidChar = false;
        }
        if (!letters && !numbers) {
            isValidChar = true; // Non-letters and non-numbers should always be valid
        }

        if (isValidChar) {
            buffer[count] = ch;
        } else {
            count++; // Increment count to avoid decrementing beyond 0
            continue;
        }
        
        // Check for surrogate pairs and adjust the buffer accordingly
        if (Character.isSurrogate(ch)) {
            if (!Character.isLowSurrogate(ch) || !Character.isHighSurrogate(buffer[count - 1])) {
                throw new IllegalArgumentException("Invalid surrogate pair: " + ch + ", " + buffer[count - 1]);
            }
            // Do not add the high or low surrogates to the buffer as they are handled separately
        } else if (ch >= 56320 && ch <= 57343) {
            // Low surrogate, insert high surrogate after putting it in
            buffer[count] = ch;
            count--;
            buffer[count] = (char) (55296 + random.nextInt(128));
        } else if (ch >= 55296 && ch <= 56191) {
            // High surrogate, insert low surrogate before putting it in
            buffer[count] = (char) (56320 + random.nextInt(128));
            count--;
            buffer[count] = ch;
        }
    }

    return new String(buffer);
}