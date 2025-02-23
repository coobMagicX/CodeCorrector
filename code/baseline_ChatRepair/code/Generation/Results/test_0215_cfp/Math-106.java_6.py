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

    if(checkIfNextCharacterIsIllegalMinus(source, pos)) {
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

    if(checkIfNextCharacterIsIllegalMinus(source, pos)) {
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

    if(den.intValue() < 0) { // Checking if the parsed denominator is negative.
        pos.setIndex(initialIndex);
        return null;
    }
    
    // calculate the fraction
    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();

    return new Fraction(((Math.abs(w) * d) + n) * MathUtils.sign(w), d);
}

// Helper method to detect illegal minus signs after numbers are parsed
private boolean checkIfNextCharacterIsIllegalMinus(String source, ParsePosition pos) {
    int startIndex = pos.getIndex();
    parseAndIgnoreWhitespace(source, pos);
    char c = parseNextCharacter(source, pos);
    boolean isIllegalMinus = c == '-';
    pos.setIndex(startIndex); // Reset position back to where it was after number parsing
    return isIllegalMinus;
}
