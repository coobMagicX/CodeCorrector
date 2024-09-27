private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        if (appendTo != null) appendTo.append(QUOTE);
        pos.setIndex(start + 1); // Move past the initial quote
    }
    int lastHold = pos.getIndex();
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && i + ESCAPED_QUOTE.length() <= pattern.length() && 
            pattern.substring(i, i + ESCAPED_QUOTE.length()).equals(ESCAPED_QUOTE)) {
            appendTo.append(c, lastHold, i - lastHold); // Append up to the escape sequence
            appendTo.append(QUOTE); // Append the escaped quote
            i += ESCAPED_QUOTE.length() - 1; // Move past the escape sequence
            lastHold = i + 1; // Set new position after the escaped quote
            pos.setIndex(i + 1); // Update the ParsePosition index correctly
        } else if (c[i] == QUOTE) {
            if (escapingOn) {
                appendTo.append(c, lastHold, i - lastHold);
                pos.setIndex(i + 1);
                return appendTo;
            } else {
                throw new IllegalArgumentException("Unterminated quoted string at position " + start);
            }
        }
    }
    if (escapingOn) {
        // Handle case where quote was escaped at the end of the pattern
        appendTo.append(c, lastHold, pattern.length() - lastHold);
        pos.setIndex(pattern.length());
    } else {
        throw new IllegalArgumentException("Unterminated quoted string at position " + start);
    }
    return appendTo;
}