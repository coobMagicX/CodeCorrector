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
                String hexCodePointStr = input.subSequence(start, end).toString();
                // Convert hexadecimal code point to integer
                entityValue = Integer.parseInt(hexCodePointStr.substring(2), 16);
                // Check if the entity value is a supplementary Unicode character
                if (entityValue > 0xFFFF) {
                    out.write(Character.toChars(entityValue));
                } else {
                    out.write((char) entityValue);
                }
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
                // Check if the entity value is a supplementary Unicode character
                if (entityValue > 0xFFFF) {
                    out.write(Character.toChars(entityValue));
                } else {
                    out.write((char) entityValue);
                }
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}