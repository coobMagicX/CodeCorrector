private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) { // Start the loop from 'start' to avoid re-indexing
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(
                    QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length()); // Correctly increment the index past the escaped quote
            lastHold = pos.getIndex();
            continue;
        }
        switch (c[pos.getIndex()]) {
        case QUOTE:
            if (!escapingOn) { // Only append the quote if not in escaping mode
                return appendTo == null ? null : appendTo.append(c, lastHold,
                        pos.getIndex() - lastHold).append(QUOTE);
            } else {
                next(pos); // Skip the quote since it is escaped
            }
        default:
            next(pos);
        }
    }
    if (escapingOn) { // Ensure that an unterminated string doesn't go unnoticed in escaping mode
        throw new IllegalArgumentException(
                "Unterminated quoted string at position " + start);
    }
    return appendTo == null ? null : appendTo.append(c, lastHold,
            pos.getIndex() - lastHold); // Append the remaining characters
}