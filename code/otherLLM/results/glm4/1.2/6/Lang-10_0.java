private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*"); // Modified to append only one asterisk
            }
            continue;
        }
        wasWhite = false;
        switch (c) {
            case '\'':
                if (unquote) {
                    if (++i == value.length()) {
                        return regex; // No need to escape quote as it is already handled by unquote logic
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
                regex.append('\\');
        }
        regex.append(c);
    }
    return regex;
}