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

        switch (c) {
            case '\'':
                if (unquote) {
                    // Check for unquoted string containing single quotes
                    int nextQuoteIndex = value.indexOf('\'', i + 1);
                    if (nextQuoteIndex == -1 || nextQuoteIndex >= value.length()) {
                        return regex; // Invalid unquoted string, stop processing
                    }
                    // Append escaped quote and the character after it
                    regex.append('\\');
                    regex.append(c);
                    // Skip the character after the single quote
                    i = nextQuoteIndex + 1;
                } else {
                    regex.append(c); // Quote is not to be unquoted, append as is
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