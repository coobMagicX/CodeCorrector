private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        // Check for an escaped quote
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length()); // Move past the escaped quote
            lastHold = pos.getIndex(); // Reset lastHold to current position after escaped quote
            continue; // Skip to next iteration of loop
        }
        
        switch (c[pos.getIndex()]) {
            case QUOTE:
                if (!escapingOn) { // If we're not escaping, process the quote
                    next(pos); // Move index past the quote character
                    return appendTo == null ? null : appendTo.append(c, lastHold,
                            pos.getIndex() - lastHold);
                }
                break; // If we are escaping and encounter a quote, just move on to next char

            default:
                next(pos); // Always advance position if not processing a quote or an escaped quote
        }
    }

    if (lastHold == start) { // Ensure that something was appended if no quotes were processed
        throw new IllegalArgumentException("No starting quote detected at position " + start);
    } else {
        return appendTo == null ? null : appendTo.append(c, lastHold, pos.getIndex() - lastHold);
    }
}