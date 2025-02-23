private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        
        // Handle whitespace by consolidating into a regex pattern that matches multiple whitespaces
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                regex.append("\\s+");
                wasWhite = true;
            }
            continue;
        }
        wasWhite = false;
        
        // When unquoting, skip the processing of escaped quotes
        if (unquote && c == '\'') {
            continue;
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
                break;
        }
        
        regex.append(c);
    }
    return regex;
}
