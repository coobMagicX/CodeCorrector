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
        case '\\': // handle escaped quote
            next(pos);
            if (escapingOn && c[pos.getIndex()] == QUOTE) { // check for quoted string within quotes
                appendTo.append(c, lastHold + 1, pos.getIndex() - lastHold).append(QUOTE); // skip the escape and quote
                pos.setIndex(i + 2); // move to after the escaped quote and the actual quote
            } else {
                appendTo.append(c, lastHold, pos.getIndex() - lastHold);
            }
            break;
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