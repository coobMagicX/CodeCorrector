public Fraction parse(String source, ParsePosition pos) {
    // try to parse improper fraction
    Fraction ret = super.parse(source, pos);
    if (ret != null) {
        return ret;
    }
    
    int initialIndex = pos.getIndex();

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);

    // parse whole
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

    // Check for whitespace after numerator that could have negative effects
    parseAndIgnoreWhitespace(source, pos);
    
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

    int w = whole.intValue(); // Whole number part must determine the sign.
    int n = num.intValue();
    int d = den.intValue();

    // Calculate signs correctly
    if (w < 0 ^ n < 0) {
        n = Math.abs(n) * -1; // Num takes the combined sign if they are different
    } else {
        n = Math.abs(n); // Otherwise, it's always positive 
    }

    return new Fraction((Math.abs(w) * d + n) * (w < 0 ? -1 : 1), Math.abs(d));
}
