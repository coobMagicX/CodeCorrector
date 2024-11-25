private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && isEscapedQuote(c, i)) {
            appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(
                    QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
            continue;
        }
        switch (c[pos.getIndex()]) {
        case QUOTE:
            next(pos);
            return appendTo == null ? null : appendTo.append(c, lastHold,
                    pos.getIndex() - lastHold);
        default:
            next(pos);
        }
    }
    if (!pattern.startsWith(ESCAPED_QUOTE, start)) { // Check for unterminated string
        throw new IllegalArgumentException(
                "Unterminated quoted string at position " + start);
    } else {
        return appendTo == null ? null : appendTo.append(pattern.substring(start));
    }
}

private boolean isEscapedQuote(char[] c, int index) {
    return pattern.startsWith(ESCAPED_QUOTE, index);
}