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

        // Check if there is a valid digit after 'x' or 'X' if it is a hex number
        if (isHex && start >= seqEnd) {
            return 0; // early return if there's nothing after "x" or "X"
        }

        int end = start;
        // Note that this supports character codes without a ';' on the end
        // The loop must also check for the bounds of the sequence
        while (end < seqEnd && input.charAt(end) != ';') {
            end++;
        }

        // If end reached the sequence end without finding ';', treat as malformed entity
        if (end == seqEnd) {
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
            System.err.println("FAIL: " + input.subSequence(start, end) + "[" + start + "][" + end + "]");
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Include length of "&#", "x" if hex, and ";" in the return value
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}