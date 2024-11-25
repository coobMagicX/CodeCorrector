private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) { // Start from 'start' instead of pos.getIndex()
        if (escapingOn && pattern.startsWith(ESCAPED_QUOTE, i)) {
            appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
        } else {
            switch (c[i]) { // Use 'i' instead of pos.getIndex()
                case QUOTE:
                    next(pos); // Ensure to move the index forward
                    appendTo.append(c, lastHold, i - lastHold);
                    return appendTo == null ? null : appendTo;
                default:
                    next(pos); // Ensure to move the index forward
            }
        }
    }
    if (lastHold < pattern.length()) { // Check for unterminated quoted string at the end
        throw new IllegalArgumentException(
                "Unterminated quoted string at position " + lastHold);
    } else {
        return appendTo == null ? null : appendTo.append(c, lastHold,
                pattern.length() - start); // Append any remaining characters
    }
}

private void next(ParsePosition pos) {
    pos.setIndex(pos.getIndex() + 1);
}