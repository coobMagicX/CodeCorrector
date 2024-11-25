private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
        StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        switch (c[i]) {
            case '\\':
                next(pos);
                if (i + 1 >= c.length) {
                    throw new IllegalArgumentException(
                            "Unterminated quoted string at position " + start);
                }
                switch (c[i + 1]) {
                    case QUOTE:
                        appendTo.append(c, lastHold, pos.getIndex() - lastHold)
                                .append(QUOTE);
                        pos.setIndex(i + 2);
                        lastHold = pos.getIndex();
                        break;
                    default:
                        next(pos);
                        appendTo.append(c, lastHold, pos.getIndex() - lastHold);
                }
                i++;
                continue;
            case QUOTE:
                if (escapingOn) {
                    next(pos);
                    appendTo.append(c, lastHold, pos.getIndex() - lastHold)
                            .append(QUOTE);
                    break;
                } else {
                    next(pos);
                    return appendTo == null ? null : appendTo.append(c,
                            lastHold, pos.getIndex() - lastHold);
                }
            default:
                next(pos);
        }
    }
    if (escapingOn && c[start] != QUOTE) {
        throw new IllegalArgumentException(
                "Unterminated quoted string at position " + start);
    }
    return appendTo == null ? null : appendTo;
}