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
                if (unquote && i < value.length() - 1) { // Check to ensure there is a character after the quote
                    c = value.charAt(++i); // Move index forward after consuming the quote
                } else if (!unquote || i == value.length() - 1) {
                    // If unquote is not set or we are at the end of the string, escape the single quote
                    regex.append('\\');
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
        if (c != '\'') { // If we haven't already appended the character, do so now
            regex.append(c);
        }
    }
    return regex;
}