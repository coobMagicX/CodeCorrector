private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            appendTo.append(c, lastHold, i - lastHold).append(QUOTE); // Corrected the bounds of substring
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
            continue;
        }
        switch (c[pos.getIndex()]) {
            case QUOTE:
                if (i > start && c[i - 1] != BACKSLASH) { // Check for an escaped quote
                    next(pos);
                    return appendTo == null ? null : appendTo.append(c, lastHold,
                            pos.getIndex() - lastHold);
                }
                break;
            default:
                next(pos);
        }
    }
    if (lastHold < pattern.length()) {
        appendTo.append(c, lastHold, pattern.length() - lastHold); // Append remaining characters
    } else {
        throw new IllegalArgumentException(
                "Unterminated quoted string at position " + start);
    }
}