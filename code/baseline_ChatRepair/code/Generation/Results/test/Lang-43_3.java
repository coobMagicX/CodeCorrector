private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    int patternLength = pattern.length();
    char[] chars = pattern.toCharArray();

    if (escapingOn && chars[start] == QUOTE) {
        if (appendTo != null) {
            appendTo.append(QUOTE);
        }
        pos.setIndex(start + 1);
        return appendTo;
    }
    
    int lastHold = start;
    for (int i = start; i < patternLength; i++) {
        
        if (escapingOn && i + ESCAPED_QUOTE.length() <= patternLength && pattern.regionMatches(i, ESCAPED_QUOTE, 0, ESCAPED_QUOTE.length())) {
            if (appendTo != null) {
                appendTo.append(chars, lastHold, i - lastHold);
                appendTo.append(QUOTE);
            }
            i += ESCAPED_QUOTE.length() - 1; 
            lastHold = i + 1;
            continue;
        }

        
        if (chars[i] == QUOTE) {
            if (appendTo != null) {
                appendTo.append(chars, lastHold, i - lastHold);
            }
            pos.setIndex(i + 1);  
            return appendTo;
