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

        // Correctly use subSequence on CharSequence, not String
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

        // Correctly write the supplementary character as a sequence of two surrogates
        if (Character.isSupplementaryCharacter(entityValue)) {
            int highSurrogate = Character.highSurrogate(entityValue);
            int lowSurrogate = Character.lowSurrogate(entityValue);
            out.write(highSurrogate);
            out.write(lowSurrogate);
        } else {
            out.write(entityValue);
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}