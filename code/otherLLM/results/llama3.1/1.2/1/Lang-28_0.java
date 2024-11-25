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
                entityValue = Integer.parseInt(new String(input.subSequence(start, end)).toLowerCase(), 16);
            } else {
                entityValue = Integer.parseInt(new String(input.subSequence(start, end)), 10);
            }
        } catch(NumberFormatException nfe) {
            // Handle invalid or malformed input
            return -1;
        }

        if(entityValue > Character.MAX_VALUE) { 
            // Handle supplementary Unicode characters
            out.write((char) (0x10000 + entityValue));
        } else {
            out.write((char) entityValue);
        }
        
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}