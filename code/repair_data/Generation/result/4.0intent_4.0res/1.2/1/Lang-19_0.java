public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    // Uses -2 to ensure there is something after the &#
    if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        if (start >= seqEnd) {
            return 0; // early exit if the string ends after &# or &#x without numbers
        }

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;

            if (start >= seqEnd) {
                return 0; // early exit if the string ends after &#x without numbers
            }
        }

        int end = start;
        // Note that this supports character codes without a ; on the end
        while (end < seqEnd && input.charAt(end) != ';') {
            end++;
        }

        // Handle the case where the ; is missing at the end of the input
        if (end >= seqEnd) {
            return 0; // incomplete entity reference should not be processed
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

        return 2 + (end - start) + (isHex ? 1 : 0) + (input.charAt(end) == ';' ? 1 : 0);
    }
    return 0;
}