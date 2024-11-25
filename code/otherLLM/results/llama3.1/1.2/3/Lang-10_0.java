private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*+");
            } else {
                regex.append(c); // append consecutive whitespace
            }
            continue;
        }
        wasWhite = false;
        
        switch (c) {
        case '\'':
            if (unquote) {
                if (++i == value.length()) {
                    return regex; // skip next character when unquoting
                }
                c = value.charAt(i);
            } else {
                regex.append('\\').append(c); // quote single quote without unquoting
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
        
        // Append quoted character if it needs to be escaped
        if (c == '\'' && unquote) {
            continue;
        } else {
            regex.append(c);
        }
    }
    
    return regex;
}