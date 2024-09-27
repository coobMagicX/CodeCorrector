public int translate(CharSequence input, int index, Writer out) throws IOException {
    // Protect from ArrayIndexOutOfBounds
    if (index < 0 || index >= input.length()) {
        return 0;
    }

    if (input.charAt(index) == '&' && index + 1 < input.length() && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        // Check bounds for start
        if (start >= input.length()) {
            return 0;
        }

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        // Ensure start is still within bounds after potential increment
        if (start >= input.length()) {
            return 0;
        }

        int end = start;
        while (end < input.length() && input.charAt(end) != ';') {
            end++;
        }

        // Check if a semicolon was found
        if (end >= input.length() || input.charAt(end) != ';') {
            return 0;
        }

        int entityValue;
        try {
            String numberStr = input.subSequence(start, end).toString();
            if (isHex) {
                entityValue = Integer.parseInt(numberStr, 16);
            } else {
                entityValue = Integer.parseInt(numberStr, 10);
            }
        } catch (NumberFormatException nfe) {
            return 0;
        }

        out.write(entityValue);
        return 2 + (end - index) - 2 + (isHex ? 1 : 0) + 1;
    }
    return 0;
}