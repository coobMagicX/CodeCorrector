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
            next(pos); // This is fine but we need to append the string before calling next(pos)
            if (appendTo != null) {
                appendTo.append(c, lastHold, pos.getIndex() - lastHold);
            }
            return appendTo == null ? null : appendTo;
        default:
            next(pos);
            break; // Ensure that we continue processing even if it's not a quote
        }
    }
    throw new IllegalArgumentException(
            "Unterminated quoted string at position " + start);
}

private void next(ParsePosition pos) {
    // Implementation of the 'next' method, which should increment the index of pos
}