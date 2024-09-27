private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
            i = pos.getIndex() - 1; // adjust i to continue from the new position
            continue;
        }
        if (c[i] == QUOTE) {
            pos.setIndex(i + 1);
            return appendTo == null ? null : appendTo.append(c, lastHold, i - lastHold);
        }
    }
    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
}