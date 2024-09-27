public int translate(CharSequence input, int index, Writer out) throws IOException {
    // Check if index is within the bounds of input
    if (index >= 0 && index < input.length() - 1 && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        // Ensure there's enough room to check for 'x' or 'X'
        if (start < input.length()) {
            char firstChar = input.charAt(start);
            if (firstChar == 'x' || firstChar == 'X') {
                start++;
                isHex = true;
            }
        }

        int end = start;

        // Ensure 'end' doesn't exceed the bounds of input
        while (end < input.length() && input.charAt(end) != ';') {
            end++;
        }

        // If no ';' was found, return 0 to indicate no valid entity was detected
        if (end == input.length()) {
            return 0;
        }

        int entityValue;
        try {
            String entity = input.subSequence(start, end).toString();
            if (isHex) {
                entityValue = Integer.parseInt(entity, 16);
            } else {
                entityValue = Integer.parseInt(entity, 10);
            }
        } catch (NumberFormatException nfe) {
            return 0;
        }

        out.write(entityValue);
        return 2 + (end - index - 2) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}