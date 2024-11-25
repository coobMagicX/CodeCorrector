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
            if(unquote && i+1 < value.length()) {
                ++i; // Skip the next character when unquoting
                c = value.charAt(i); // Use the next character after the single quote
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