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
            if(isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start + 1, end).toString(), 10);
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        // Add validation to ensure entityValue is a valid Unicode code point
        if (entityValue < 0 || entityValue > 0x10FFFF) {
            throw new IllegalArgumentException("Invalid Unicode code point: " + entityValue);
        }
        
        out.write(Character.toChars(entityValue));
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}