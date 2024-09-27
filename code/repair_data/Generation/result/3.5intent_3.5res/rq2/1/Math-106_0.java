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
    
    // minus signs should be leading, invalid expression
    
    // check for minus sign
    int startIndex = pos.getIndex();
    boolean minusSign = false;
    char sign = parseNextCharacter(source, pos);
    if (sign == '-') {
        minusSign = true;
    } else {
        pos.setIndex(initialIndex);
        return null;
    }
    
    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    
    // parse numerator
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null) {
        // invalid integer number
        // set index back to initial, error index should already be set
        // character examined.
        pos.setIndex(initialIndex);
        return null;
    }
    
    // minus signs must be leading, invalid
    
    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();
    
    if (minusSign) {
        w = -w;
    }
    
    return new Fraction(((Math.abs(w) * d) + n) * MathUtils.sign(w), d);
}