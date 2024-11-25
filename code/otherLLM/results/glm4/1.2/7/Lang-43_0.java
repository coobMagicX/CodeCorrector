private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            // Append the escaped quote and update index
            appendTo.append(c, lastHold, pos.getIndex() - lastHold);
            appendTo.append(QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
        } else if (c[pos.getIndex()] == QUOTE) {
            // Handle the quote character by skipping it and moving to next character
            next(pos);
            // Append the characters from the hold position to the current index minus one
            appendTo.append(c, lastHold, pos.getIndex() - lastHold);
        } else {
            // If not a quote or escaped quote, move to the next character
            next(pos);
        }
    }
    // Append any remaining characters from the last hold position to the end of the string
    if (lastHold < pattern.length()) {
        appendTo.append(c, lastHold, pos.getIndex() - lastHold);
    } else {
        // If no characters were processed, throw an exception indicating unprocessed input
        throw new IllegalArgumentException("Unterminated quoted string at position " + start);
    }
    return appendTo;
}

private void next(ParsePosition pos) {
    if (pos.getIndex() < pattern.length()) {
        pos.setIndex(pos.getIndex() + 1);
    } else {
        // If we reach the end of the string, throw an exception indicating a mismatch
        throw new IllegalArgumentException("Unexpected end of string");
    }
}