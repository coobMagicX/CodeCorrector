private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite= false;
    for(int i= 0; i<value.length(); ++i) {
        char c= value.charAt(i);
        if(Character.isWhitespace(c)) {
            if(!wasWhite) {
                wasWhite= true;
                regex.append("\\s*+");
            } else {
                regex.append(c); // append whitespace directly
            }
            continue;
        }
        wasWhite= false;
        switch(c) {
        case '\'':
            if(unquote && i + 1 < value.length()) { // check index bounds before incrementing
                if(++i==value.length()) {
                    return regex; // return immediately when at end of string
                }
                c= value.charAt(i);
            } else {
                break;
            }
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
        if(c != '\'') { // prevent appending duplicate quote
            regex.append(c);
        }
    }
    return regex;
}