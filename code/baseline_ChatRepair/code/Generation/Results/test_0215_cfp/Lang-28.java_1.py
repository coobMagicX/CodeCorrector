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

        // If no ';' found, or no digits between markers
        if (end >= input.length() || end == start) {
            // Output the original characters from '&' to current position
            out.write(input.subSequence(index, end).toString());
            return end - index;
        }

        int entityValue = 0;
        // Parse the integer value between 'start' and 'end'
        String entity = input.subSequence(start, end).toString();
        try {
            if (!entity.isEmpty()) {
                if (isHex) {
                    entityValue = Integer.parseInt(entity, 16);
                } else {
                    entityValue = Integer.parseInt(entity, 10);
                }
                if (entityValue > 0xFFFF) {
                    // High code point, needs handling with surrogate pairs
                    out.write(Character.toChars(entityValue));
                } else {
                    out.write(entityValue);
                }
                return 2 + (end - start) + (isHex ? 1 : 0) + 1;
            } else {
                // No numeric value, write out the original sequence
                out.write(input.subSequence(index, end + 1).toString());
                return end + 1 - index;
            }
        } catch (NumberFormatException nfe) {
            // In case of NumberFormatException, output the original characters
            out.write(input.subSequence(index, end + 1).toString());
            return end + 1 - index;
        }
    }
    return 0;
}
