public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    // Use -2 to ensure there is something after the &#, but also consider entities without semi-colon
    if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;
        boolean foundSemiColon = false;

        if (start >= seqEnd) { return 0; } // Check bounds after identifying 'x' or 'X'

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
            if (start >= seqEnd) { return 0; } // Check bounds for hex without semi-colon
        }

        int end = start;
        while (end < seqEnd && input.charAt(end) != ';') {
            end++;
        }
        
        if (end < seqEnd && input.charAt(end) == ';') {
            foundSemiColon = true;
        }

        if (end == start) { // If no characters between start and end
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
            System.err.println("FAIL: " + input.subSequence(start, end) + "[" + start + "]["+ end + "]");
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // if semi-colon was found adjust for its length otherwise handle without semi-colon
        if (foundSemiColon) {
            return 2 + (end - start) + (isHex ? 1 : 0) + 1;
        } else {
            return 2 + (end - start) + (isHex ? 1 : 0);
        }
    }
    return 0;
}
