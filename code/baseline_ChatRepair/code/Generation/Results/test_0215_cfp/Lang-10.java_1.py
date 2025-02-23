private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    boolean inQuote = false;

    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        // Check if we are entering or exiting a quoted segment
        if (unquote && c == '\'') {
            if (inQuote && i + 1 < value.length() && value.charAt(i + 1) == '\'') {  // handle escaped single quote inside quote
                regex.append("\\'");
                i++; // skip next quote
            } else {
                inQuote = !inQuote; // toggle inQuote state
            }
            continue;
        }
        
        // If inside a quote and unquote is true, add character as it is
        if (unquote && inQuote) {
            regex.append(c);
            continue;
        }
        
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s+");
            }
            continue;
        }
        wasWhite = false;

        // Escape special regex characters if not inside a quote
        switch(c) {
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
