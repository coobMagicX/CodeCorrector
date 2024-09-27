private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);

        // Check and handle white spaces
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*");  // Use '\s*' instead of '\s+' to correctly handle multiple or no whitespace characters
            }
            continue;
        }
        wasWhite = false;

        // Handle unquoting characters
        if (c == '\'' && unquote) {
            if (++i == value.length()) {
                return regex;
            }
            c = value.charAt(i); // Read the next character after unquoting
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