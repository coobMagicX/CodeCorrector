public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if (index < input.length() && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        while (end < input.length() && input.charAt(end) != ';') {
            end++;
        }

        // Protect against ArrayIndexOutOfBoundsException by ensuring the subSequence does not go out of bounds
        String entityValueStr = input.subSequence(start, Math.min(end, input.length())).toString();
        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(entityValueStr, 16);
            } else {
                entityValue = Integer.parseInt(entityValueStr, 10);
            }
        } catch (NumberFormatException nfe) {
            return 0;
        }

        // Use Character.toChars to properly handle supplementary characters
        char[] chars = Character.toChars(entityValue);

        out.write(chars);
        return 2 + (end - start) + (isHex ? 1 : 0) + chars.length; // Include the length of the written characters
    }
    return 0;
}