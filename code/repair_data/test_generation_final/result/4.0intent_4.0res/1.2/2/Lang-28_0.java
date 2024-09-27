public int translate(CharSequence input, int index, Writer out) throws IOException {
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
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        // Write the entityValue as UTF-8 to handle surrogate pairs correctly
        if (Character.isSupplementaryCodePoint(entityValue)) {
            out.write(Character.highSurrogate(entityValue));
            out.write(Character.lowSurrogate(entityValue));
        } else {
            out.write(entityValue);
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}