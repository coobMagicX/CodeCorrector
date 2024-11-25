public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if (index < 0 || index >= input.length()) {
        throw new IndexOutOfBoundsException("Character index is out of bounds");
    }

    if (input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        // Protect against ArrayIndexOutOfBounds by ensuring end does not exceed the CharSequence length
        int end = Math.min(input.length(), start + 4); // Assuming a max of 4 hex digits for the highest value in Unicode (0x10FFFF)
        while (input.charAt(end) != ';' && end < input.length()) {
            end++;
        }

        if (end == input.length() || input.charAt(end) != ';') {
            throw new IllegalArgumentException("Invalid character entity reference");
        }

        int entityValue;
        try {
            // Adjust the parsing to correctly handle Unicode code points beyond U+FFFF
            String numericValue = input.subSequence(start, end).toString();
            if (isHex) {
                entityValue = Integer.parseInt(numericValue, 16);
            } else {
                entityValue = Integer.parseInt(numericValue, 10);
            }
        } catch (NumberFormatException nfe) {
            return 0;
        }

        // Check if the code point is in the supplementary plane and write it accordingly
        if (entityValue > 0xFFFF) {
            out.write((char)(entityValue - 0x10000) + ((entityValue >> 16) & 0xFF));
            out.write((char)((entityValue >> 10) & 0xFF));
        } else {
            out.write(entityValue);
        }
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}