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
            // If not, this would indicate a malformed reference, so we should handle it accordingly
            if (start >= seqEnd) {
                System.err.println("FAIL: Malformed hexadecimal entity reference at index " + start);
                return 0;
            }
        }

        int end = start;
        // Note that this supports character codes without a ; on the end
        boolean incompleteEntity = true; // Assume it's an incomplete entity until proven otherwise

        while (incompleteEntity && input.charAt(end) != ';') {
            if (!Character.isDigit(input.charAt(end)) && input.charAt(end) != 'x' && input.charAt(end) != 'X') {
                // If we reach a character that is neither a digit nor 'x'/'X', it's an incomplete entity
                System.err.println("FAIL: Incomplete numeric entity reference at index " + end);
                return 0;
            }
            end++;
        }

        if (incompleteEntity) {
            // We've reached the end of the CharSequence without finding a semicolon, which means the entity is incomplete
            System.err.println("FAIL: Incomplete numeric entity reference at index " + seqEnd);
            return 0;
        }

        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 16); // Subtract 1 to not include the semicolon
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 10); // Subtract 1 to not include the semicolon
            }
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end - 1) + "[" + start + "," + (end - 1) + "]");
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Adjust the length of the entity for correct index increment
        return end - start;
    }
    return 0;
}