private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);

        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*");  // Correctly match any whitespace characters
            }
            continue;
        }
        wasWhite = false;

        if (unquote && c == '\'') {
            // Skip over the next character if it's part of the unquote requirement
            if (++i < value.length()) {
                c = value.charAt(i);  // Move to the character after the single quote
            } else {
                return regex;  // End of string processing prematurely, return current result
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
            default:
                break;  // Handle normal characters without alteration
        }
        regex.append(c);
    }
    return regex;
}
