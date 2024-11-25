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
    
    // Check for a minus sign and ensure it's leading
    char sign = source.charAt(pos.getIndex() - 1);
    if (sign == '-') {
        pos.setIndex(pos.getIndex() - 1); // Move back to the sign
        num = getNumeratorFormat().parse(source, pos); // Parse again as negative number
    } else {
        // Reset index to after whole number for proper fraction parsing
        int numeratorIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        if (pos.getIndex() != numeratorIndex) { // If we parsed something, it's not a leading minus
            num = getNumeratorFormat().parse(source, pos);
            if (num == null || source.charAt(pos.getIndex()) != '-') {
                pos.setIndex(initialIndex);
                throw new ParseException("Leading minus expected", pos);
            }
        } else {
            // No whitespace after whole number, this is a proper fraction
            num = new Integer(0); // Proper fractions have numerator 0
        }
    }

    // parse '/'
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    switch (c) {
    case 0 :
        // no '/'
        // return num as a fraction
        if (num == new Integer(0)) { // Check for proper fraction
            return ret; // If there's no numerator, it's already parsed properly
        } else {
            throw new ParseException(" '/' expected", pos);
        }
    case '/' :
        // found '/', continue parsing denominator
        break;
    default :
        // invalid '/'
        // set index back to initial, error index should be the last
        // character examined.
        pos.setIndex(initialIndex);
        pos.setErrorIndex(startIndex);
        throw new ParseException("Invalid format", pos);
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
    if (d == 0) {
        throw new ParseException("Denominator cannot be zero", pos);
    }
    return new Fraction(((Math.abs(w) * d) + n) * MathUtils.sign(w), d);
}