private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            // Ensure to escape ' ' after a non-whitespace character and before the next whitespace
            if (!wasWhite && c != ' ') {
                regex.append('\\').append(c).append("\\s*");
                wasWhite = true;
            } else if (c == ' ') { // Handle consecutive spaces as single ' '
                if (!wasWhite) {
                    regex.append(" ");
                    wasWhite = true;
                }
            }
        } else {
            // Reset the flag when a non-whitespace character is encountered
            wasWhite = false;

            switch (c) {
                case '\'':
                    if (unquote) {
                        if (++i >= value.length()) { // Check if it goes beyond string length
                            return regex;
                        }
                        c = value.charAt(i); // Get the next character to unquote
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
                    // Always escape these characters even if unquoting is enabled
                    regex.append('\\');
            }
            // Append the character to the regex string builder after it's been checked and possibly escaped
            regex.append(c);
        }
    }
    return regex;
}