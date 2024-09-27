private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
                                        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        if (appendTo != null) {
            appendTo.append(QUOTE);
        }
        pos.setIndex(start + 1); // Move index past the initial quote
    }
    int lastHold = pos.getIndex();
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            // Append the text from the last hold to the start of the escaped quote
            appendTo.append(c, lastHold, i - lastHold);
            // Append a single quote instead of an escaped quote
            appendTo.append(QUOTE);
            // Move the index past the escaped quote
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex(); // Update lastHold to the new position
            i = pos.getIndex() - 1; // Adjust 'i' as pos.getIndex() has been updated
            continue;
        }
        if (c[i] == QUOTE) {
            // If we encounter a closing quote
            if (escapingOn && i + 1 < pattern.length() && c[i + 1] == QUOTE) {
                // If it's an escaped quote within the quoted string
                i++; // Increment i to skip the next quote
                continue; // Skip this character, it will be handled in the next iteration
            } else {
                // Close the quoted string
                appendTo.append(c, lastHold, i - lastHold);
                pos.setIndex(i + 1);
                return appendTo;
            }
        }
    }
    throw new IllegalArgumentException(
            "Unterminated quoted string at position " + start);
}