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
            appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(
                    QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
            continue;
        }
        switch (c[pos.getIndex()]) {
        case QUOTE:
            next(pos);
            if (escapingOn && i < pattern.length() - 1 && c[i + 1] == QUOTE) {
                appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(
                        QUOTE).append(QUOTE);
                pos.setIndex(i + 2);
                return appendTo;
            }
            return appendTo == null ? null : appendTo.append(c, lastHold,
                    pos.getIndex() - lastHold);
        default:
            next(pos);
        }
    }
    if (start == pattern.length() - 1 && c[start] == QUOTE) {
        throw new IllegalArgumentException("Unterminated quoted string at position " + start);
    } else {
        return appendTo == null ? null : appendTo.append(c, lastHold,
                pos.getIndex() - lastHold);
    }
}