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

    // check for leading minus sign in whole part
    if (whole.intValue() < 0) {
        throw new ParseException("Invalid expression: Leading minus sign detected in whole part", initialIndex);
    }
    
    // parse numerator
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        // invalid integer number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }

    // check for leading minus sign in numerator
    if (num.intValue() < 0) {
        throw new ParseException("Invalid expression: Leading minus sign detected in numerator", initialIndex);
    }
    
    // parse '/'
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    switch (c) {
    case 0 :
        // no '/'
        // return num as a fraction
        return new Fraction(num.intValue(), 1);
    case '/' :
        // found '/', continue parsing denominator
        break;
    default :
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
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }
    
    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();
    return new Fraction(((Math.abs(w) * d) + n) * MathUtils.sign(w), d);
}