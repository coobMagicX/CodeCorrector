private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        
        // Handle consecutive whitespace characters
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*+");
            }
            continue;
        }
        wasWhite = false;
        
        // Handle quote character when unquoting is enabled
        if (c == '\'' && unquote) {
            if (++i >= value.length()) {
                break; // Safely exit if the increment leads to the end of the string
            }
            c = value.charAt(i); // Take the next character after the quote
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
                regex.append('\\'); // Append escape character
                break;
        }
        
        regex.append(c);
    }
    return regex;
}