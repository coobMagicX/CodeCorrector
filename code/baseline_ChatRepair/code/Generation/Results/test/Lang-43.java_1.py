private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();

    // Early exit case: check if position at the quote while escaping on.
    if (escapingOn && c[start] == QUOTE) {
        pos.setIndex(start + 1);
        if (appendTo != null && (start + 1 < pattern.length() && c[start + 1] == QUOTE)) {
            appendTo.append(QUOTE); // For escaping, '' to ' conversion
            pos.setIndex(start + 2); // skip past '' to continue
            start += 2;
        } else {
            return appendTo;  // No appending just move past ending quote
        }
    }

    int lastHold = start;
    for (int i = start; i < pattern.length(); i++) {
        if (escapingOn && i + 1 < pattern.length() && c[i] == QUOTE && c[i + 1] == QUOTE) {
            // Encountered escaped quote, append what's before and the quote itself.
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold).append(QUOTE);
            }
            i++; // Skip the second quote of the escaped quotes
            lastHold = i + 1; // Move past the escaped part
            continue;
        }
        if (c[i] == QUOTE) {
            // Append last part and skip the quote, then return
            if (appendTo != null) {
                appendTo.append(c, lastHold, i - lastHold);
            }
            pos.setIndex(i + 1);
            return appendTo;  // end function after processing until quote
        }
    }
    // Append any remaining characters if no closing quote found
    if (appendTo != null && lastHold < pattern.length()) {
        appendTo.append(c, lastHold, pattern.length() - lastHold);
    }
    pos.setIndex(pattern.length()); // Moves index to end since parsing is complete
    
    return appendTo;
}
