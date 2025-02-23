private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);

        // Handle whitespace
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s+");
            }
            continue;
        }
        wasWhite = false;
        
        // Handle characters that need special treatment when inside quotes
        if (unquote && c == '\'') {
            if (++i == value.length()) {
                return regex;
            }
            c = value.charAt(i); // Fetch the next character after the quote
            if (i + 1 < value.length() && value.charAt(i + 1) == '\'') {
                // Continuous single quotes should be treated literally.
                regex.append("\\'");
                i++; // Skip the next quote that was part of escaping
                continue;
            }
        } else {
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
                    regex.append('\\');
                    break;
            }
        }
        regex.append(c);
    }
    return regex;
}
