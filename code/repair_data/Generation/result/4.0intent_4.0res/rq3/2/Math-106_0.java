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
        pos.setIndex(initialIndex);
        return null;
    }
    
    // parse '/'
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    if (c == 0) {
        // no '/'
        // return num as a fraction which is whole number
        return new Fraction(whole.intValue() * num.intValue(), 1);
    } else if (c != '/') {
        // invalid '/'
        // set index back to initial, error index should be the last
        // character examined.
        pos.setIndex(initialIndex);
        pos.setErrorIndex(startIndex);
        return null;
    }

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);

    // parse denominator
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null) {
        // invalid integer number
        // set index back to initial, error index should already be set
        pos.setIndex(initialIndex);
        return null;
    }

    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();

    // Calculate the correct sign and value for the fraction
    if (w < 0) {
        n = -n;  // adjust the numerator based on the whole part's sign
    }

    return new Fraction(w * d + n, d);
}