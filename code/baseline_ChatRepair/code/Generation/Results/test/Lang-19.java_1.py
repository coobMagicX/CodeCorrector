public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        // Checking if starting character is 'x' or 'X' for hexadecimal
        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        // Looking for the next ';'
        while (end < seqEnd && input.charAt(end) != ';') {
            end++;
        }

        // If no semicolon is found before the end of the sequence, revert to printing the original sequence
        if (end == seqEnd || input.charAt(end) != ';') {
            out.write(input.subSequence(index, end).toString());
            return end - index;
        }

        String entity = input.subSequence(start, end).toString();
        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(entity, 16);
            } else {
                entityValue = Integer.parseInt(entity, 10);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: Parsing error for entity: " + entity);
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Length calculation including all specials characters '&' '#' 'x' if present and ';' plus digits
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }

    // If the sequence does not start with "&#", simply return zero progress
    return 0;
}
