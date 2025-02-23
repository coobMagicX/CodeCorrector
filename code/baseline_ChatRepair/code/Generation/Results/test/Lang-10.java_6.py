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
        if (unquote && c == '\'') {
            if (++i >= value.length()) {  // Ensure i does not exceed length
                --i;  // Decrement i to reevaluate the current character
                continue;
            }
            c = value.charAt(i);  // Only update c if i is valid
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
        }
        regex.append(c);
    }
    return regex;
}
