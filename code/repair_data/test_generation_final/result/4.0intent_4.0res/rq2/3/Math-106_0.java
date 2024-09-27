public Fraction parse(String source, ParsePosition pos) {
    // try to parse improper fraction
    Fraction ret = super.parse(source, pos);
    if (ret != null) {
        // early return if parsing as improper fraction succeeds
        return ret;
    }
    
    int initialIndex = pos.getIndex();

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);

    // parse whole
    Number whole = getWholeFormat().parse(source, pos);
    if (whole == null) {
        // invalid integer number
        pos.setIndex(initialIndex);
        return null;
    }

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);
    
    // parse numerator
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        // invalid integer number
        pos.setIndex(initialIndex);
        return null;
    }
    
    // Check for invalid minus sign in the numerator
    if (source.substring(initialIndex, pos.getIndex()).trim().contains("-")) {
        pos.setIndex(initialIndex);
        return null;
    }

    // parse '/'
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    switch (c) {
        case 0:
            // no '/', return num as a fraction
            return new Fraction(num.intValue(), 1);
        case '/':
            // found '/', continue parsing denominator
            break;
        default:
            // invalid '/'
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
        pos.setIndex(initialIndex);
        return null;
    }
    
    // Check for invalid minus sign in the denominator
    if (source.substring(startIndex, pos.getIndex()).trim().contains("-")) {
        pos.setIndex(initialIndex);
        return null;
    }

    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();

    // Calculate the mixed fraction as an improper fraction
    return new Fraction(((Math.abs(w) * d) + n) * Math.signum(w), d);
}