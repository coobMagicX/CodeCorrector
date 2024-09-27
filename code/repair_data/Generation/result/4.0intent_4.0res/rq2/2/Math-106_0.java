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
        // invalid integer number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    
    // parse numerator
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        // invalid integer number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }
    
    // parse '/'
    int slashIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    if (c != '/') {
        // no '/' or invalid character
        // set index back to initial, error index should be the last character examined.
        pos.setIndex(initialIndex);
        pos.setErrorIndex(slashIndex);
        return null;
    }

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);

    // parse denominator
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null || den.intValue() == 0) {
        // invalid integer number or zero denominator
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }

    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();

    // Adjust signs for the numerator as per whole number sign
    if (w < 0) {
        n = -Math.abs(n);
    }

    return new Fraction(((Math.abs(w) * d) + n) * MathUtils.sign(w), d);
}