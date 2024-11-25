private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) { // Start loop from 'start' to avoid infinite loop
        char currentChar = c[i];
        if ((escapingOn && currentChar == QUOTE) || (currentChar == ESCEPVED_QUOTE)) {
            appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(QUOTE);
            pos.setIndex(i + 1); // Increment index by one past the escaped quote or actual quote
            lastHold = pos.getIndex();
        }
    }
    if (lastHold == start) { // If no quotes were found and the loop didn't run, throw exception
        throw new IllegalArgumentException("Unterminated quoted string at position " + start);
    } else {
        appendTo.append(c, lastHold, pos.getIndex() - lastHold); // Append remaining characters from 'lastHold' to current index
    }
    return appendTo;
}