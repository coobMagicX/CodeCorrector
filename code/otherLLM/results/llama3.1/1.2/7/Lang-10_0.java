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
                if(++i < value.length()) {
                    c = value.charAt(i);
                    break;
                } else {
                    regex.append(c);
                    break;
                }
            }
            regex.append("\\'");
            break;
        default:
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
            }
            break;
        }
        regex.append(c);
    }
    return regex;
}