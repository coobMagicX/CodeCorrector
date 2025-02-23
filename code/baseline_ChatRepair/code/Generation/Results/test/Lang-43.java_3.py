private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    
    if (escapingOn && c[start] == QUOTE) {
        // Move past the initial quote
        pos.setIndex(pos.getIndex() + 1);
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    
    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        if (escapingOn && i + ESCAPED_QUOTE.length() <= pattern.length() && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            appendTo.append(c, lastHold, i - lastHold).append(QUOTE); // Correctly append characters before the escaped quote
            pos.setIndex(i + ESCAPED_QUOTE.length()); // Correctly move past the escaped quote.
            i = pos.getIndex(); // Set loop index to the correct position after escaped quote.
            lastHold = i; // Update lastHold to the new position.
            continue;
        }

        if (c[i] == QUOTE) {
            pos.setIndex(i + 1);
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold); // Append text excluding the closing quote.
            }
            return appendTo;
        }
    }

    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
}

private void next(ParsePosition pos) {
    pos.setIndex(pos.getIndex() + 1);
}
