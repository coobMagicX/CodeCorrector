private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        // Skip the initial quote if escaping is on
        pos.setIndex(start + 1);
        if (appendTo != null) {
            appendTo.append(QUOTE);
        }
        return appendTo;
    }
    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            // Append text before the escaped quote
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold);
                appendTo.append(QUOTE); // Append single quote instead of escaped version
            }
            i += ESCAPED_QUOTE.length() - 1;
            pos.setIndex(i + 1);
            lastHold = pos.getIndex();
            continue;
        }
        if (c[i] == QUOTE) {
            pos.setIndex(i + 1); // Move index past the quote
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold);
            }
            return appendTo;
        }
    }
    // If the loop completes without returning, the string was unterminated
    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
}