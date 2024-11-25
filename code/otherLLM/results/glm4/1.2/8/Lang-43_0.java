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
            appendTo.append(c, lastHold, pos.getIndex() - lastHold)
                      .append(QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
            continue;
        }
        switch (c[pos.getIndex()]) {
            case QUOTE:
                next(pos);
                appendTo.append(c, lastHold, pos.getIndex() - lastHold);
                return appendTo == null ? null : appendTo;
            default:
                next(pos);
                if (appendTo != null) {
                    appendTo.append(c, lastHold, pos.getIndex() - lastHold);
                }
        }
    }
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    throw new IllegalArgumentException(
            "Unterminated quoted string at position " + start);
}

private void next(ParsePosition pos) {
    // Assuming this method increments the index of ParsePosition by one.
    pos.setIndex(pos.getIndex() + 1);
}