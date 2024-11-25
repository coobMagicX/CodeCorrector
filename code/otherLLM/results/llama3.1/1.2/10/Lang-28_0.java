public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if(input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = Math.min(index + 2, input.length()); // added length check
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if(firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        while(end < input.length() && input.charAt(end) != ';') { // added end < input.length()
            end++;
        }

        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
            
            // Corrected handling for supplementary characters
            if(entityValue > 0xFFFF) { 
                out.write(0x10000 + (entityValue - 0x10000));
            } else {
                out.write(entityValue);
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}