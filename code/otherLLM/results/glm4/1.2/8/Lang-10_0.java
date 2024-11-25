private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                // Fixed issue by appending "\\s*+" only once when a whitespace character is encountered
                regex.append("\\s*+");
            }
            continue;
        } else {
            wasWhite = false; // Reset the flag after a non-whitespace character is processed
        }

        switch (c) {
            case '\'':
                if (unquote) {
                    if (++i == value.length()) {
                        return regex; // Fixed issue by returning early when at the end of the string
                    }
                    c = value.charAt(i);
                }
                break;
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
                regex.append('\\'); // No change here, it is correct to escape these characters
        }
        regex.append(c);
    }
    return regex;
}