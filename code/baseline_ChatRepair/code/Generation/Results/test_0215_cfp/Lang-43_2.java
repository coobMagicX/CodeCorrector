private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();

    
    if (escapingOn && c[start] == QUOTE) {
        pos.setIndex(start + 1); 
        if (appendTo != null) {
            appendTo.append(QUOTE); 
        }
        return appendTo;
    }

    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        
        if (escapingOn && i + ESCAPED_QUOTE.length() <= pattern.length() && pattern.substring(i, i + ESCAPED_QUOTE.length()).equals(ESCAPED_QUOTE)) {
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold); 
                appendTo.append(QUOTE); 
            }
            i += ESCAPED_QUOTE.length() - 1; 
            lastHold = i + 1; 
            continue;
        }

        
        if (c[i] == QUOTE) {
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold); 
            }
            pos.setIndex(i + 1