public int translate(CharSequence input, int index, Writer out) throws IOException {
    // Protect from ArrayIndexOutOfBounds
    if(index + 1 < input.length() && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        if(start < input.length()) {
            char firstChar = input.charAt(start);
            if(firstChar == 'x' || firstChar == 'X') {
                start++;
                isHex = true;
            }
        }

        int end = start;
        while(end < input.length() && input.charAt(end) != ';') {
            end++;
        }

        if(end >= input.length()) { // Check if ';' was found
            return 0;
        }

        int entityValue;
        try {
            String numberStr = input.subSequence(start, end).toString();
            if(isHex) {
                entityValue = Integer.parseInt(numberStr, 16);
            } else {
                entityValue = Integer.parseInt(numberStr, 10);
            }

            if (Character.isValidCodePoint(entityValue)) {
                if (entityValue > 0xFFFF) {
                    // Write supplementary characters
                    char[] surrogates = Character.toChars(entityValue);
                    out.write(surrogates[0]);
                    out.write(surrogates[1]);
                } else {
                    out.write(entityValue);
                }
            } else {
                return 0;
            }

            return 2 + (end - start) + (isHex ? 1 : 0) + 1;
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }
    return 0;
}