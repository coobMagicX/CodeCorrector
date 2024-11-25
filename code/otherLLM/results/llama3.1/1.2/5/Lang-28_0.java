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

        String entityValueStr = input.subSequence(start, end).toString();
        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(entityValueStr, 16);
            } else {
                entityValue = Integer.parseInt(entityValueStr, 10);
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        // Check if the calculated entityValue is a supplementary Unicode character
        int codePoint = Character.codePointAt(input, start - 2); // Subtract 2 to account for '&' and '#'
        if (Character.isSupplementaryCodePoint(codePoint)) {
            out.write(entityValue);
        } else {
            return 0; // Return 0 if the entityValue does not correspond to a supplementary Unicode character
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}