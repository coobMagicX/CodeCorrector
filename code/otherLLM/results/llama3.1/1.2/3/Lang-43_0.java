private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && c[i] == ESCAPE && c[i + 1] == QUOTE) {
            appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            pos.setIndex(i + 2);
            lastHold = pos.getIndex();
            continue;
        }
        switch (c[pos.getIndex()]) {
        case QUOTE:
            if (escapingOn && c[i] == ESCAPE) {
                next(pos);
                return appendTo == null ? null : appendTo.append(c, lastHold,
                        pos.getIndex() - lastHold).append(QUOTE);
            } else {
                next(pos);
                return appendTo == null ? null : appendTo.append(c, lastHold,
                        pos.getIndex() - lastHold);
            }
        default:
            next(pos);
        }
    }
    throw new IllegalArgumentException(
            "Unterminated quoted string at position " + start);
}