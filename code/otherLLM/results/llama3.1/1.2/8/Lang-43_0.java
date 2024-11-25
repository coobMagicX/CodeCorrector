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
        switch (c[i]) { // corrected index to 'i'
        case QUOTE:
            if (escapingOn) {
                next(pos); // increment position only when escaping is on
                return appendTo == null ? null : appendTo.append(c, lastHold,
                        pos.getIndex() - lastHold);
            } else {
                next(pos);
                throw new IllegalArgumentException(
                        "Unterminated quoted string at position " + start);
            }
        default:
            next(pos);
        }
    }
    throw new IllegalArgumentException(
            "Unterminated quoted string at position " + start);
}