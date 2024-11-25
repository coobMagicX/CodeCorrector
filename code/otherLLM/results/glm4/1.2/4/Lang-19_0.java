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

            // Check there's more than just an x after the &#
            if (start >= seqEnd) { // Check if there's no valid character after "x" or "X"
                return 0;
            }
        }

        int end = start;
        // Note that this supports character codes without a ; on the end
        while (input.charAt(end) != ';' && end < seqEnd - 1) { // Also check end is within bounds to avoid index out of range
            end++;
        }

        if (end == seqEnd) {
            // No semicolon found, return without writing anything
            return 0;
        }

        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 10); // Subtract 1 to ignore the last character which is ';'
            }
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end - 1) + "[" + start +"][" + (end - 1) + "]");
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Adjust the index to account for the character just written
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}