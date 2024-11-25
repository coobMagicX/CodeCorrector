public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if(index < 0 || index >= input.length()) {
        return 0; // Return early to avoid ArrayIndexOutOfBoundsException
    }

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
        // Protect from ArrayIndexOutOfBoundsException
        if(end >= input.length()) {
            return 0; // Return early to avoid ArrayIndexOutOfBoundsException
        }

        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
        } catch(NumberFormatException nfe) {
            return 0; // Return early to avoid further exceptions
        }

        // Write the character if it is a valid Unicode code point
        if(entityValue >= 0 && entityValue <= Character.MAX_CODE_POINT) {
            out.write(entityValue);
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}