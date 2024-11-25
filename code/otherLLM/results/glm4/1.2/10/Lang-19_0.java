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

            // Check there's more than just an x after the &#
            if(input.length() <= start + 1) {
                System.err.println("ERROR: Incomplete hex reference at " + index);
                return 0;
            }
        }

        int end = start;
        // Note that this supports character codes without a ; on the end
        while(end < seqEnd && input.charAt(end) != ';') {
            end++;
        }

        // Check if there is a semicolon after the number to complete the reference
        if(end == seqEnd || input.charAt(end) != ';') {
            System.err.println("ERROR: Incomplete numeric character reference at " + index);
            return 0;
        }

        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 16); // Corrected end to be one before semicolon
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end - 1).toString(), 10);
            }
        } catch(NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end - 1) + "[" + start +"]["+ (end - 1) +"]");
            return 0;
        }

        if(entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Adjust the end index to account for the semicolon
        end++;

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}