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

        int end = start;
        // Ensure there is a semicolon to properly terminate the numeric character reference
        while (end < seqEnd && (input.charAt(end) != ';')) {
            end++;
        }
        
        if (end == seqEnd || input.charAt(end) != ';') { // If no semicolon found, flag it
            System.err.println("Incomplete numeric character reference: " + input.subSequence(start, end));
            return 0;
        }

        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 10);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end) + "[" + start +"]["+ end +"]");
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }
        
        // Adjust the length to include the semicolon
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}