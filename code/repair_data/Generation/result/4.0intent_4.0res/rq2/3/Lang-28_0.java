public int translate(CharSequence input, int index, Writer out) throws IOException {
    if (index + 1 >= input.length() || input.charAt(index) != '&' || input.charAt(index + 1) != '#') {
        return 0;
    }

    int start = index + 2;
    boolean isHex = false;

    if (start >= input.length()) {
        return 0;
    }

    char firstChar = input.charAt(start);
    if (firstChar == 'x' || firstChar == 'X') {
        start++;
        isHex = true;
    }

    if (start >= input.length()) {
        return 0;
    }

    int end = start;
    while (end < input.length() && input.charAt(end) != ';') {
        end++;
    }

    if (end >= input.length()) {
        return 0; // No terminating ';', return 0 as failure to translate
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
        return 0; // Invalid number format
    }

    if (Character.isValidCodePoint(entityValue)) {
        if (Character.isSupplementaryCodePoint(entityValue)) {
            char[] surrogatePairs = Character.toChars(entityValue);
            out.write(surrogatePairs, 0, surrogatePairs.length);
        } else {
            out.write(entityValue);
        }
    } else {
        return 0; // Invalid Unicode code point
    }

    return 2 + (end - start) + (isHex ? 1 : 0) + 1; // Correctly compute the length of the parsed entity
}