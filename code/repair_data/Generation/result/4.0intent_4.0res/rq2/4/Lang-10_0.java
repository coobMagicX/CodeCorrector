private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);

        // Handle whitespace compression into a single regex pattern
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s+");
            }
            continue;
        }
        wasWhite = false;

        // Check if the character should be unquoted
        if (c == '\'' && unquote) {
            if (i + 1 < value.length()) {
                i++;
                c = value.charAt(i);  // Read next character after unquote
            } else {
                // If no more characters after the quote, skip appending
                continue;
            }
        }

        // Escape special regex characters
        switch (c) {
            case '?':
            case '[':
            case ']':
            case '(':
            case ')':
            case '{':
            case '}':
            case '\\':
            case '|':
            case '*':
            case '+':
            case '^':
            case '$':
            case '.':
                regex.append('\\');  // Ensure the character is escaped
                break;
        }
        regex.append(c);
    }
    return regex;
}