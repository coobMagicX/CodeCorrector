private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
                                        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        pos.setIndex(start + 1); // Move past the quote
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            i += ESCAPED_QUOTE.length() - 1; // Move index to end of escaped quote
            pos.setIndex(i + 1); // Update position
            lastHold = pos.getIndex(); // Reset lastHold
            continue;
        }
        if (c[i] == QUOTE) {
            pos.setIndex(i + 1); // Move past the quote
            return appendTo == null ? null : appendTo.append(c, lastHold, i - lastHold);
        }
    }
    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
}

private void next(ParsePosition pos) {
    pos.setIndex(pos.getIndex() + 1);
}