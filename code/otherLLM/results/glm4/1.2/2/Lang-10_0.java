private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*+");
            }
            continue;
        }
        wasWhite = false;

        // Escape special regex characters
        switch (c) {
            case '\'':
                if (unquote) { // If unquoting is enabled, skip the next character if it's a single quote
                    if (++i < value.length()) {
                        c = value.charAt(i);
                    } else {
                        return regex; // End of string reached after unquoting a single quote
                    }
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
                regex.append('\\');
        }

        // Append the character to the regex string builder
        regex.append(c);
    }
    return regex;
}