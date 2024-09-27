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

        // Check that we have at least one more character after 'x' or 'X' for hexadecimal or after '#' for decimal
        if (start >= seqEnd || (isHex && start >= seqEnd)) {
            return 0; // Not enough characters to parse a valid numerical entity
        }

        int end = start;
        // Note that this supports character codes without a ';' on the end
        while (end < seqEnd && input.charAt(end) != ';') {
            end++;
        }

        // If there is no ';' to terminate the entity, it is not valid
        if (end == seqEnd || input.charAt(end) != ';') {
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

        // We return the length of the entire entity sequence including starting '&', ending ';', and 'x' if it is hex
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    // If the initial conditions are not met, just return 0 indicating no characters processed
    return 0;
}