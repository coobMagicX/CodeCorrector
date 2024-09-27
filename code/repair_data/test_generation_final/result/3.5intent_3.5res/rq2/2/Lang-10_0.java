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
                if (unquote) {
                    if (++i == value.length()) {
                        return regex;
                    }
                    c = value.charAt(i);
                    regex.append(c);
                } else {
                    regex.append(c);
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
            default:
                regex.append(c);
        }
    }
    return regex;
}

public boolean addRegex(FastDateParser parser, StringBuilder regex) {
    regex.append(validTimeZoneChars);
    return true;
}