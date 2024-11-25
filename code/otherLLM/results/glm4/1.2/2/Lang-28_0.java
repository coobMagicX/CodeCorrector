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

        // Parse the NCR into an integer safely considering supplementary characters
        try {
            String entityValueStr = input.subSequence(start, end).toString();
            if (isHex) {
                entityValueStr = entityValueStr.toUpperCase(); // Ensure hex is uppercase for parsing
            }
            
            int codePoint;
            if (entityValueStr.length() > 5 && isHex && Character.isDigit(entityValueStr.charAt(0)) && Character.isDigit(entityValueStr.charAt(1))) {
                // Handle potential supplementary characters
                codePoint = Integer.parseInt(entityValueStr, 16);
                char[] cArray = Character.toChars(codePoint);
                for (char c : cArray) {
                    out.write(c);
                }
            } else {
                // Write the BMP character if no supplementary characters are needed
                codePoint = Character.codePointAt(input, start, end);
                out.write(codePoint);
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}