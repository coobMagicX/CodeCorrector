public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    
    if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        // Checking if starting character is x or X for hexadecimal
        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        while (end < seqEnd && input.charAt(end) != ';') {
            end++;
        }

        // Handle the case where no ';' was found till the end of the string
        if (end == seqEnd) {
            System.err.println("FAIL: Incomplete entity reference found at index " + index);
            return 0;
        }

        int entityValue;
        try {
            String entity = input.subSequence(start, end).toString();
            if (isHex) {
                entityValue = Integer.parseInt(entity, 16);
            } else {
                entityValue = Integer.parseInt(entity, 10);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: Parsing error at " + input.subSequence(start, end) + "[" + start + "][" + end + "]");
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Include ';' in the count (+1), Add 2 for initial '&' and '#', Include 'x' or 'X' if isHex
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }

    return 0;
}
