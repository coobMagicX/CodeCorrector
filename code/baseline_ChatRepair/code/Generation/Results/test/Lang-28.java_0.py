public int translate(CharSequence input, int index, Writer out) throws IOException {
    // Check for '&' followed by '#'
    if (index <= input.length() - 2 && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        // Ensure that the character at 'end' is within bounds and look for ';'
        while (end < input.length() && input.charAt(end) != ';') {
            end++;
        }

        // Guard against missing ';' that leads to 'end' being at the limit
        if (end >= input.length()) {
            throw new IllegalArgumentException("Parsing error: missing ';' terminator");
        }

        int entityValue = 0;
        // Parse the integer value between 'start' and 'end'
        try {
            String entity = input.subSequence(start, end).toString();
            if (isHex) {
                entityValue = Integer.parseInt(entity, 16);
            } else {
                entityValue = Integer.parseInt(entity, 10);
            }
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Parsing error: invalid numeric entity reference");
        } 

        // Writing the character to the output depending on its code point
        if (entityValue > 0xFFFF) {
            // High code point, we need to convert this into a surrogate pair
            out.write(Character.toChars(entityValue));
        } else {
            out.write(entityValue);
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}
