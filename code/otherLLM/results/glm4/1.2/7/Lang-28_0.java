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

        int entityValue;
        try {
            String valueString = input.subSequence(start, end).toString();
            if (isHex) {
                entityValue = Integer.parseInt(valueString, 16);
            } else {
                entityValue = Integer.parseInt(valueString, 10);
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        // Write the character or characters
        if (entityValue <= 0xFFFF) {
            out.write(entityValue);
        } else {
            // Handle supplementary characters
            int highSurrogate = entityValue - 0x10000;
            int lowSurrogate = entityValue - (highSurrogate + 0xD800);
            out.write((char)(0xD800 + highSurrogate));
            out.write((char)(0xDC00 + lowSurrogate));
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}