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

    // parse '/'
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    if (c != '-') { // Expecting a minus sign after numerator
        pos.setIndex(initialIndex);
        pos.setErrorIndex(startIndex);
        throw new ParseException("Invalid fraction format", source, pos);
    }

    // Continue parsing the remainder of the denominator as it should start with a minus sign
    int denStartIndex = pos.getIndex();
    char nextChar = parseNextCharacter(source, pos);

    if (nextChar == '-') { // Denominator cannot have two consecutive minus signs
        pos.setIndex(initialIndex);
        pos.setErrorIndex(denStartIndex + 1);
        throw new ParseException("Invalid fraction format", source, pos);
    }

    // If the first character after the numerator is not a digit or zero,
    // it means the expression is invalid
    if (!Character.isDigit(nextChar) && nextChar != '0') {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(denStartIndex + 1);
        throw new ParseException("Invalid fraction format", source, pos);
    }

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