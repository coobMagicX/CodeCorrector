private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s+");
            }
            continue;
        }
        wasWhite = false;
        // Handle unquoting within the string values.
        if (unquote && c == '\'') {
            if (i + 1 < value.length()) {
                c = value.charAt(++i);
            } else {
                continue;
            }
        }
        
        // Escape all regex special characters to treat them as literal characters
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
                break;
        }
        
        regex.append(c);
    }
    return regex;
}
