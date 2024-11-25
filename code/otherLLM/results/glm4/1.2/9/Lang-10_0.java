private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*"); // Removed the '+' to avoid unnecessary repetition
            }
            continue;
        }
        wasWhite = false;
        switch (c) {
            case '\'':
                if (unquote) {
                    if (++i < value.length()) { // Fixed the condition to check for end of string
                        c = value.charAt(i);
                    } else {
                        // If the unquote logic is wrong, it should return an empty regex or handle the error accordingly.
                        return new StringBuilder(); // Example handling: returning empty regex
                    }
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