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

    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();

    // Handling sign if only 'whole' is negative, malfunction might be here if 'num' part was to carry an unhandled negative alone
    boolean negative = w < 0 || (whole.intValue() == 0 && n < 0);
    if(w < 0 && n < 0) {  // This checks if both are negative which should not be allowed
        pos.setIndex(initialIndex);
        pos.setErrorIndex(startIndex);
        return null;
    }

    // Normalize: always add absolute values of numerator; adjust sign at the end
    n = Math.abs(n);
    d = Math.abs(d);

    // Calculate fraction correctly considering the whole number might also be negative
    n = Math.abs(w) * d + n;  // Calculate as if all are positives
    n = negative ? -n : n;   // Set the proper sign

    return new Fraction(n, d);
}
