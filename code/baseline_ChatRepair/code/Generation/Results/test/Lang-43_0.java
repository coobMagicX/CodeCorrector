private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();

    if (escapingOn && c[start] == QUOTE) {
        if (start + 1 < c.length && c[start + 1] == QUOTE) {
            if (appendTo != null) {
                appendTo.append(QUOTE); 
            }
            pos.setIndex(start + 2); 
            return appendTo;
        } else {
            
            pos.setIndex(start + 1);
            return appendTo;
        }
    }

    int lastHold = start;
    int i = start;
    while (i < pattern.length()) {
        if (escapingOn && i + 1 < c.length && c[i] == QUOTE && c[i + 1] == QUOTE) {
            
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            }
            i += 2; 
            lastHold = i;
        } else if (c[i] == QUOTE) {
            
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold);
            }
            pos.setIndex(i + 1);
            return appendTo;
        } else