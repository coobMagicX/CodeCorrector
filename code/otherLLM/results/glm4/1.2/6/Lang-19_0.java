public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    // Uses -2 to ensure there is something after the &#
    if(input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if(firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        // Support character codes without a ; on the end by moving to the next non-semicolon character
        while(end < seqEnd && (input.charAt(end) != ';')) 
        {
            end++;
        }

        // If we reach the end of the input, it's an incomplete entity
        if(end == seqEnd) {
            // Handle incomplete entity by simply writing out what has been read so far
            int tempIndex = index + 2; // Skip the '&' and '#'
            while(tempIndex < start) { // Write up to the first non-escaped character after the &#
                out.write(input.charAt(tempIndex++));
            }
            return 0;
        }

        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 10); // Exclude the semicolon
            }
        } catch(NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end) + "[" + start +"]["+ end +"]");
            return 0;
        }

        if(entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        return 2 + (end - start) + (isHex ? 1 : 0); // Add back the character for 'x' or 'X'
    }
    return 0;
}