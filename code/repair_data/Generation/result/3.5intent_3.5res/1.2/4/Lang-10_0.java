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

        if (c == '\'') {
            if (unquote) {
                if (++i == value.length()) {
                    return regex;
                }
                c = value.charAt(i);
            }
        } else if (c == '?' || c == '[' || c == ']' || c == '(' || c == ')' || c == '{' || c == '}' ||
                c == '\\' || c == '|' || c == '*' || c == '+' || c == '^' || c == '$' || c == '.') {
            regex.append('\\');
        }

        regex.append(c);
    }
    return regex;
}