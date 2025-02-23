private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                regex.append("\\s+");
                wasWhite = true;
            }
        } else {
            wasWhite = false;
            if (unquote && c == '\'') {
                // Skip beginning and ending quotes
                if (i == 0 || i == value.length() - 1) {
                    continue;
                } else {
                    // Check if the current quote is for escaping purposes
                    // This checks the next character to see if it's the same to confirm it's escaped
                    if (value.charAt(i - 1) == '\\' || (i + 1 < value.length() && value.charAt(i + 1) == c)) {
                        regex.append(c); // Treat as a regular character
                        continue;
                    }
                }
            }
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
                    regex.append('\\'); // Escape the Regex meta char
            }
            regex.append(c);
        }
    }

    return regex;
}
