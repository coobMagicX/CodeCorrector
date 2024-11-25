public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    // Uses -2 to ensure there is something after the &#
    if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        // Ensure there's more than just an x after the &#
        if (!isHex && input.charAt(start) == '0') {
            start++; // Skip leading zeros for decimal numbers
        }

        int end = start;
        // Note that this supports character codes without a ; on the end
        while (input.charAt(end) != ';') {
            if (end >= seqEnd - 1) { // Check for incomplete reference
                System.err.println("ERROR: Incomplete numeric character reference at position " + start);
                out.write('?'); // Replace with a default character, such as '?'
                return 0;
            }
            end++;
        }

        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 10); // Subtract 1 to exclude the semicolon
            }
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: Invalid numeric character reference at position " + start);
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Adjust the index and length to include the semicolon
        return 2 + (end - start) - 1 + (isHex ? 1 : 0) + 1;
    }
    return 0;
}