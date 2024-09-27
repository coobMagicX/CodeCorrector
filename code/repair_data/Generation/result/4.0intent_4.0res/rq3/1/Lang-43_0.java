private StringBuffer appendQuotedString(String pattern, ParsePosition pos, 
                                        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    final char QUOTE = '\''; // Assuming QUOTE is a single quote character
    final String ESCAPED_QUOTE = "''"; // Assuming ESCAPED_QUOTE is two single quotes

    if (escapingOn && c[start] == QUOTE) {
        if (appendTo == null) return null;
        pos.setIndex(start + 1); // Skip the initial quote
        return appendTo.append(QUOTE);
    }

    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        if (escapingOn && i + 1 < pattern.length() && pattern.substring(i, i + 2).equals(ESCAPED_QUOTE)) {
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            }
            i += 1; // Skip over the second quote in the escaped quote
            pos.setIndex(i + 1);
            lastHold = pos.getIndex();
            continue;
        }
        if (c[i] == QUOTE) {
            pos.setIndex(i + 1);
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold);
            }
            return appendTo;
        }
    }
    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
}