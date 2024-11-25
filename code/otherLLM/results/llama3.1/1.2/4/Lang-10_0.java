private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite= false;
    for(int i= 0; i<value.length(); ++i) {
        char c= value.charAt(i);
        if(Character.isWhitespace(c)) {
            if(!wasWhite) {
                wasWhite= true;
                regex.append("\\s*+");
            }
            continue;
        }
        wasWhite= false;
        switch(c) {
        case '\'':
            if(unquote) {
                if(++i==value.length()) {
                    return regex;
                }
                c= value.charAt(i);
            } else {
                // Added check for unquote
                regex.append("\\'");
            }
            break;
        case '\\':
            // Add backslash before '\'
            regex.append("\\\\");
            break;
        case '?':
        case '[':
        case ']':
        case '(':
        case ')':
        case '{':
        case '}':
        case '|':
        case '*':
        case '+':
        case '^':
        case '$':
        case '.':
            regex.append('\\');
        }
        if (c != '\'') { // Add check to prevent append(c) when c is '\' and unquote is false
            regex.append(c);
        }
    }
    return regex;
}