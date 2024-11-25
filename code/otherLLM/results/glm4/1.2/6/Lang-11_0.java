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

    // Ensure start and end are within bounds, and at least one of letters or numbers is true
    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
            start = 0; // Assuming the char array's first character should be the starting point
        } else {
            if (!letters && !numbers) {
                throw new IllegalArgumentException("No valid range specified and no characters provided");
            } else if (letters) {
                start = ' ';                
                end = 'z' + 1;
            } else if (numbers) {
                start = '0';
                end = '9';
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
        
        // Handle the surrogate pairs
        if ((ch >= 56320 && ch <= 57343) || (ch >= 55296 && ch <= 56191)) {
            int highSurrogate, lowSurrogate;
            if (ch >= 56320 && ch <= 57343) { // Low surrogate
                highSurrogate = ch - 56320 + 0xD800;
                buffer[count] = ch;
                count--;
                if (count < 0) {
                    throw new IllegalArgumentException("Insufficient length for a valid string with surrogates.");
                }
            } else { // High surrogate
                lowSurrogate = ch - 55296 + 0xDC00;
                highSurrogate = lowSurrogate - 0x0020; // Adjust for the 20 value difference between surrogates
                buffer[count] = (char) highSurrogate;
                count--;
            }
            if (count < 0) {
                throw new IllegalArgumentException("Insufficient length for a valid string with surrogates.");
            }
            buffer[count] = (char) lowSurrogate;
        } else if (ch >= 56192 && ch <= 56319) { // Private high surrogate
            // Skip the private high surrogate
            continue;
        } else {
            buffer[count] = ch;
        }

        // Ensure characters meet the conditions specified by letters and numbers
        if (letters && !Character.isLetter(ch)
                || numbers && !Character.isDigit(ch)) {
            count++; // Adjust count to re-select this position in case it's an invalid character
        }
    }
    
    // If we have a negative gap, some characters may not be selected, check for that.
    if (count < 0) {
        throw new IllegalArgumentException("Insufficient length for a valid string with surrogates.");
    }

    return new String(buffer);
}