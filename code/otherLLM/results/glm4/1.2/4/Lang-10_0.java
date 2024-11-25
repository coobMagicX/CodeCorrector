private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*+"); // This should be escaped to avoid conflict with regex pattern
                continue;
            }
        } else {
            wasWhite = false;
        }

        switch (c) {
            case '\'':
                if (unquote) {
                    if (++i == value.length()) {
                        return regex; // Should append the single quote directly if not at end
                        continue;
                    }
                    c = value.charAt(i);
                } else {
                    regex.append('\\');
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
                break;
        }
        regex.append(c);
    }
    return regex;
}