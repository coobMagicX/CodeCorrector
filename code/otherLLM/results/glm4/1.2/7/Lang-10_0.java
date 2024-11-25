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

        // Check for single quote to potentially unquote
        if (c == '\'') {
            if (unquote) {
                if (++i < value.length()) { // Corrected condition, should be less than or equal to length
                    c = value.charAt(i);
                } else {
                    i--; // Decrement i to handle the single quote at the end properly
                }
            }
        }

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

    // Handle the end of the string if it ends with a single quote that needs unquoting
    if (wasWhite && c == '\'') {
        regex.setLength(regex.length() - 2); // Remove the last escaped whitespace and the backslash added for the single quote
    }
    
    return regex;
}