public Fraction parse(String source, ParsePosition pos) {
    // try to parse improper fraction
    Fraction ret = super.parse(source, pos);
    if (ret != null) {
        return ret;
    }
    
    int initialIndex = pos.getIndex();

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);

    // parse whole including sign
    int startWholeIndex = pos.getIndex();
    parseNextCharAndCheckSign(source, pos); // New method to handle signs correctly
    Number whole = getWholeFormat().parse(source, pos);
    if (whole == null) {
        pos.setIndex(initialIndex);
        return null;
    }

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    
    // parse numerator
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        pos.setIndex(initialIndex);
        return null;
    }
    
    // parse '/'
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    if (c != '/') {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(startIndex);
        return null;
    }

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);

    // parse denominator
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null) {
        pos.setIndex(initialIndex);
        return null;
    }

    int sign = parseSign(source, startWholeIndex); // Determine the sign based on the parsing start index for the whole number
    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();
    
    return new Fraction(((Math.abs(w) * d) + n) * sign, d);
}

private int parseSign(String source, int index) {
    if (index > 0 && source.charAt(index - 1) == '-') {
        return -1;
    }
    return 1;
}

private void parseNextCharAndCheckSign(String source, ParsePosition pos) {
    int index = pos.getIndex();
    while (index < source.length()) {
        char ch = source.charAt(index);
        if (!Character.isWhitespace(ch)) {
            if (ch == '-') {
                pos.setIndex(index + 1);
                return;
            }
            break;
        }
        index++;
    }
    pos.setIndex(index);
}
