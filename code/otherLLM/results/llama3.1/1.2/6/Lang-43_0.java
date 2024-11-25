private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    
    // validate input by checking if pos is initialized
    if (pos == null || pos.getIndex() < 0 || pos.getIndex() >= pattern.length()) {
        throw new IllegalArgumentException("Invalid ParsePosition object");
    }
    
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
            return appendTo == null ? null : appendTo.append(c, lastHold,
                    pos.getIndex() - lastHold);
        default:
            next(pos);
        }
    }
    throw new IllegalArgumentException(
            "Unterminated quoted string at position " + start);
}