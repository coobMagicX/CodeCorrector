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
            if(unquote && i < value.length() - 1) { // Handle unquoted single quotes correctly
                i++; // Skip the next character
                c = value.charAt(i); // Use the correct character
            } else {
                regex.append("\\'");
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