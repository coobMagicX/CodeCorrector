private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    int patternLength = pattern.length();
    
    if (escapingOn && c[start] == QUOTE) {
        if (appendTo != null) {
            appendTo.append(QUOTE);
        }
        pos.setIndex(start + 1);
        return appendTo;
    }

    int lastHold = start;
    for (int i = start; i < patternLength; i++) {
        if (escapingOn && i + ESCAPED_QUOTE.length() <= patternLength && pattern.startsWith(ESCAPED_QUOTE, i)) {
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            }
            i += ESCAPED_QUOTE.length() - 1; 
            pos.setIndex(i + 1); 
            lastHold = pos.getIndex();
            continue;
        }

        if (c[i] == QUOTE) {
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold);
            }
            pos.setIndex(i + 1);
            return appendTo;
        }
    }

    throw new IllegalArgumentException("Unterminated quoted string at position "