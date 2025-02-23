private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    
    if (escapingOn && c[start] == QUOTE) {
        // Check if it is an escaped quote (double quote)
        if (start + 1 < pattern.length() && c[start + 1] == QUOTE) {
            // Handle escaped quote: treat two consecutive quotes as one quote
            if (appendTo != null) {
                appendTo.append(QUOTE);
            }
            pos.setIndex(start + 2); // move past the two quotes
            return appendTo;
        }
        // If not an escaped quote, just move past the initial quote
        start += 1;
    }

    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        if (escapingOn && c[i] == QUOTE) {
            if (i + 1 < pattern.length() && c[i + 1] == QUOTE) {
                // This is an escaped quote within the string
                if (appendTo != null) {
                    appendTo.append(c, lastHold, i - lastHold) // add text before quote
                            .append(QUOTE);  // add single quote
                }
                i += 1;  // skip the next quote
                lastHold = i + 1;  // move lastHold to the character after the escaped quote
            } else {
                // It is an end-quote which terminates this quoted segment
                if (appendTo != null) {
                    appendTo.append(c, lastHold, i - lastHold);  // append the text within the quotes
                }
                pos.setIndex(i + 1);  // move the position past the quote
                return appendTo;
            }
        }
    }

    // if we exit the loop without finding a closing quote, treat it as an error
    pos.setIndex(start);
    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
}
