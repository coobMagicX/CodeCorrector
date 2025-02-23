private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    int length = pattern.length();
    
    if (escapingOn && c[start] == QUOTE) {
        
        pos.setIndex(start + 1);
        if (appendTo != null) {
            appendTo.append(QUOTE);
        }
        return appendTo;
    }

    int lastHold = start;
    for (int i = start; i < length; i++) {
        
        if (escapingOn && (i + ESCAPED_QUOTE.length() <= length) && pattern.startsWith(ESCAPED_QUOTE, i)) {
            
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            }
            
            i += ESCAPED_QUOTE.length() - 1;
            pos.setIndex(i + 1);
            lastHold = pos.getIndex();
        } else if (c[i] == QUOTE) {
            
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold);
            }
            pos.setIndex(i + 1);
            return appendTo