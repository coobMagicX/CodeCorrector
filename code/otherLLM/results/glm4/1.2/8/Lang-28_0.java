public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if(index < 0 || index >= input.length()) {
        throw new IllegalArgumentException("Invalid index");
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
        while(end < input.length() && input.charAt(end) != ';') {
            end++;
        }

        if(end >= input.length()) { // Protect from missing semicolon
            return 0;
        }

        int entityValue;
        try {
            String valueStr = input.subSequence(start, end).toString();
            if(isHex) {
                entityValue = Integer.parseInt(valueStr, 16);
            } else {
                entityValue = Integer.parseInt(valueStr, 10);
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        // Check for supplementary character
        if(entityValue >= 0x10000 && entityValue <= 0x10FFFF) {
            int highSurrogate = (entityValue - 0x10000) + 0xD800;
            int lowSurrogate = (entityValue - 0x10000) + 0xDC00;
            out.write(highSurrogate);
            out.write(lowSurrogate);
        } else {
            out.write(entityValue);
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}