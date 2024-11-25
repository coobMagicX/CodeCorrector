public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if(input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if(firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        while(input.charAt(end) != ';') {
            end++;
        }

        // Correctly create a substring to avoid passing the subSequence method an incorrect string
        String entityValueStr = input.subSequence(start, end).toString();

        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(entityValueStr, 16);
            } else {
                entityValue = Integer.parseInt(entityValueStr, 10);
            }
        } catch(NumberFormatException nfe) {
            return 0; // This might be incorrect. We should log the error or throw an appropriate exception.
        }

        // Correctly handle writing the character to the output stream for supplementary characters
        if (entityValue >= Character.MIN_HIGH_SURROGATE && entityValue <= Character.MAX_HIGH_SURROGATE) {
            out.write(Character.toChars(entityValue));
        } else if (entityValue > Character.MIN_VALUE && entityValue < Character.MAX_VALUE) {
            // Normal character write
            out.write(entityValue);
        } else {
            // Handle invalid or out-of-range entity value
            return 0;
        }

        // Increment the index to account for the processed sequence of characters
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}