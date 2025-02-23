private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    int length = value.length();
    
    for (int i = 0; i < length; i++) {
        char c = value.charAt(i);

        // Handle consecutive whitespace with a regex that matches multiple spaces
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                regex.append("\\s+");
            }
            wasWhite = true;
            continue;
        } else {
            wasWhite = false;
        }

        // Handle characters that might be part of escape sequences in regex
        if (c == '\'' && unquote) {
            // Move past the quote character if unquote is true
            if (i + 1 < length) {
                i++;
                c = value.charAt(i);  // Proceed with the next character after the quote
            } else {
                // If it is the last character and it is a quote and unquote is true, do nothing
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
                regex.append('\\');
                break;
        }

        regex.append(c);
    }

    return regex;
}
