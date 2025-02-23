private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    int i = 0;
    while (i < value.length()) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                regex.append("\\s*+");
            }
            wasWhite = true;
        } else {
            wasWhite = false;
            if (unquote && c == '\'') {
                if (++i >= value.length()) {
                    break; // Avoid going past the end if the terminating quote is the last character
                }
                c = value.charAt(i); // Get the next character after the quote
            }
            // Escape special characters in regex
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
                    break; // Keep the break INSIDE the switch but OUTSIDE the case of the apostrophe.
            }
            regex.append(c);
        }
        i++;
    }
    return regex;
}
