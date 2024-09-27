private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s+");
            }
            continue;
        }
        wasWhite = false;
        if (unquote && c == '\'') {
            if (i + 1 == value.length()) {
                return regex;
            }
            c = value.charAt(++i);
        } else {
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
                default: // No default action required
            }
        }
        regex.append(c);
    }
    return regex;
}