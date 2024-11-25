private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && pattern.startsWith(ESCAPED_QUOTE, i)) {
            appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(
                    QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
            continue;
        }
        switch (c[pos.getIndex()]) {
        case QUOTE:
            next(pos); // This should increment the index
            appendTo.append(c, lastHold, pos.getIndex() - lastHold);
            break;
        default:
            next(pos); // This should increment the index
            break;
        }
    }
    if (lastHold != start) { // Only throw exception if we have started appending quoted string
        return appendTo == null ? null : appendTo.append(c, lastHold, pos.getIndex() - lastHold);
    } else {
        throw new IllegalArgumentException(
                "Unterminated quoted string at position " + start);
    }
}

// Helper method to increment the ParsePosition index by one.
private void next(ParsePosition pos) {
    pos.setIndex(pos.getIndex() + 1);
}