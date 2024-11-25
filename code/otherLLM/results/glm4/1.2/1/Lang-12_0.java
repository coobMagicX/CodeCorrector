public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }

    // Ensure 'start' and 'end' are within the bounds of char values
    if (start < Character.MIN_VALUE || end > Character.MAX_VALUE || start >= end) {
        throw new IllegalArgumentException("Invalid range: start=" + start + ", end=" + end);
    }

    if (start == 0 && end == 0) {
        if (!letters && !numbers) {
            end = Integer.MAX_VALUE;
        } else {
            end = Character.isUpperCase('Z') ? 'Z' : 'z';
            start = Character.isUpperCase('A') ? 'A' : 'a';                
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

        // Check for valid character before adding to buffer
        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch))) {
            buffer[count] = ch;
        } else if (!letters && !numbers) {
            buffer[count] = ch;
        } else {
            count++; // Skip invalid characters and continue with the next iteration
        }

        // Handle surrogate pairs for valid letters and digits
        if (Character.isSurrogate(ch)) {
            int highSurrogate = Character.codePointAt(chars, count);
            int lowSurrogate = Character.codePointAt(chars, count + 1);

            // Adjust buffer to include both surrogates
            buffer[count] = (char) highSurrogate;
            buffer[++count] = (char) lowSurrogate;
        }
    }

    return new String(buffer);
}