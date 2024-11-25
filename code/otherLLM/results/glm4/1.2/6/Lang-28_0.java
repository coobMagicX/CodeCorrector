public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if (index < 0 || index >= input.length() - 2) { // Check for valid index range
        return 0;
    }
    if (input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        while (input.charAt(end) != ';') {
            end++;
        }
        end++; // Move past the semicolon

        if (end > input.length()) { // Check for valid entity length
            return 0;
        }

        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
        } catch (NumberFormatException nfe) {
            return 0;
        }

        // Check if the numeric value is within the range of a supplementary character
        if ((entityValue < 0xD800 || entityValue > 0xDBFF) &&
            (entityValue != 0x0000 && entityValue <= 0xFFFF)) {
            // It's a BMP character, write it as a pair of bytes
            out.write(entityValue);
        } else if (entityValue >= 0xDC00 && entityValue <= 0xDFFF) {
            // It's a high surrogate, check for the corresponding low surrogate
            if (start - index == 2 || input.charAt(start - 3) != '#') { // Check for double-encoded
                return 0;
            }
            int highSurrogate = Integer.parseInt(input.subSequence(index + 1, start).toString(), 16);
            if (highSurrogate < 0xDC00 || highSurrogate > 0xDFFF) {
                return 0;
            }
            // Write the complete character
            out.write(0xD800 | ((entityValue - 0xDC00) << 10));
            int lowSurrogate = Integer.parseInt(input.subSequence(start + 1, end).toString(), 16);
            if (lowSurrogate < 0xE000 || lowSurrogate > 0xFFFF) {
                return 0;
            }
            out.write(0xDC00 | (lowSurrogate - 0xD800));
        } else {
            return 0; // Invalid entity
        }

        return end - index + 1; // Return length including the semicolon
    }
    return 0;
}