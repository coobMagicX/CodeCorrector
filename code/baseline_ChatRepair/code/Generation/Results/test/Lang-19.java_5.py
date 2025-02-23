public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    // Check if the input starts with '&#'.
    if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        if (start >= seqEnd) return 0; // Check bounds after identifying '#'

        // Check for hexadecimal indicator 'x' or 'X'.
        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
            if (start >= seqEnd) return 0; // Check bounds for hex content
        }

        int end = start;
        // Find the end of the numerical sequence or the entire input length.
        while (end < seqEnd && Character.isDigit(input.charAt(end)) || (isHex && isHexDigit(input.charAt(end)))) {
            end++;
        }

        // If there is no ';' after the number or the end of the CharSequence is reached, return 0.
        if (end == seqEnd || input.charAt(end) != ';') {
            return 0;
        }

        // Parse the entity value.
        int entityValue;
        try {
            String number = input.subSequence(start, end).toString();
            entityValue = isHex ? Integer.parseInt(number, 16) : Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end) + "[" + start + "]["+ end + "]");
            return 0;
        }

        // Write the Unicode character to the output.
        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Include length of '&' and '#' and possibly 'x' or 'X' and ';' in the returned index shift.
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}

// Utility method to check if a char is a valid hexadecimal digit.
private boolean isHexDigit(char c) {
    return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
}
