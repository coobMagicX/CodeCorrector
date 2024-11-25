private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i, i + 2).equals(ESCAPED_QUOTE)) { // Corrected this line to compare substring of length 2
            appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(
                    QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
            continue;
        }
        switch (c[i]) {
        case QUOTE:
            next(pos);
            return appendTo == null ? null : appendTo.append(c, lastHold,
                    pos.getIndex() - lastHold);
        default:
            next(pos);
        }
    }
    throw new IllegalArgumentException(
            "Unterminated quoted string at position " + start);
}