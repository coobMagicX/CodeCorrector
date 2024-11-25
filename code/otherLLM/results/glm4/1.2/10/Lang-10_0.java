private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s"); // Only append "\\s" instead of "\\s*+"
            }
            continue;
        } else if (c == '\'') { // Check for single quote outside the isWhitespace condition
            if (unquote && ++i < value.length()) { // Ensure we do not go out of bounds
                c = value.charAt(i);
            } else {
                unquote = false; // Reset unquote flag since it has been processed
            }
        }

        wasWhite = false;
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
                regex.append('\\'); // Correctly escape special regex characters
        }
        regex.append(c); // Append the processed character to the regex
    }
    return regex;
}