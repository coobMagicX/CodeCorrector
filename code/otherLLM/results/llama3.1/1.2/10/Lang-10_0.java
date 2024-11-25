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

        // Consolidate special character escaping
        switch (c) {
            case '\'':
                if (unquote && ++i < value.length()) {
                    c = value.charAt(i);
                }
                break;
            default:
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
                }
        }

        // Unquote single quotes
        if (c == '\'') {
            if (!unquote) {
                regex.append(c);
            } else {
                if (++i < value.length()) {
                    c = value.charAt(i);
                }
            }
        }

        regex.append(c);
    }
    return regex;
}