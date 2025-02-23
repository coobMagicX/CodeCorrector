public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    
    if (index < seqEnd - 1 && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        // Check for hexadecimal indicator 'x' or 'X'.
        if (start < seqEnd && (input.charAt(start) == 'x' || input.charAt(start) == 'X')) {
            start++;
            isHex = true;
        }

        if (start >= seqEnd) {
            // No digits after '&#' or '&#x', so treat as literal characters.
            return 0;
        }

        int end = start;
        while (end < seqEnd && Character.isDigit(input.charAt(end)) ||
               (isHex && ((input.charAt(end) >= 'a' && input.charAt(end) <= 'f') || 
                         (input.charAt(end) >= 'A' && input.charAt(end) <= 'F')))) {
            end++;
        }

        // If end is not pointing to a semicolon, validate it's still reasonable to output the entity
        if (end == seqEnd || input.charAt(end) != ';') {
            // If no valid digits or hexadecimal characters found, return 0 to skip this.
            if (end == start) {
                return 0;
            }
        }

        // Parsing the number from the sequence.
        try {
            String entityString = input.subSequence(start, end).toString();
            int entityValue = isHex ? Integer.parseInt(entityString, 16) : Integer.parseInt(entityString, 10);
            if (entityValue > 0xFFFF) {
                char[] chrs = Character.toChars(entityValue);
                out.write(chrs[0]);
                out.write(chrs[1]);
            } else {
                out.write(entityValue);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL in numeric conversion: " + input.subSequence(start, end) + "[" + start +"]["+ end +"]");
            // Handle number format exception by skipping this.
            return 0;
        }

        // Include the characters processed, plus 2 for '&#', plus 1 for 'x' if hexadecimal, and 1 if there was a semicolon.
        return 2 + (end - start) + (isHex ? 1 : 0) + (end < seqEnd && input.charAt(end) == ';' ? 1 : 0);
    }
    return 0; // Not an entity so no characters processed.
}
