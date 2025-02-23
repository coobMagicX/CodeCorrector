private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    if (start >= pattern.length()) { // early exit if already at the end
        return appendTo;
    }
    
    char[] c = pattern.toCharArray();
    
    // Skip initial quote if present and if escaping is on.
    if (escapingOn && c[start] == QUOTE) {
        if (++start == pattern.length()) { // Move past the quote and check bounds again
            return appendTo; // nothing more to append
        }
        pos.setIndex(start);
    }
    
    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            // Append the text before the escaped quote and the quote itself
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            }
            i += ESCAPED_QUOTE.length() - 1; // Move to character after ESCAPED_QUOTE
            lastHold = i + 1;
            pos.setIndex(lastHold);
            if (lastHold >= pattern.length()) {
                return appendTo; // end of string after processing escaped
            }
            continue;
        }
        
        if (c[i] == QUOTE) {
            // Append the text until the quote and stop since pattern ends quote
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold);
            }
            pos.setIndex(i + 1); // Move past the quote
            return appendTo;
        }
    }
    pos.setIndex(start); // Reset index to allow outer handling or error
    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
}
