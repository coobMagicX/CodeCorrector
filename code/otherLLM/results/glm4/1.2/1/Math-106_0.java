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
    
    // minus signs must be leading in numerator, invalid expression

    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    if (c != '-') {
        // Invalid format: No minus sign for the numerator
        pos.setIndex(initialIndex);
        return null;
    }

    // parse next character to check if it's a digit or whitespace after the minus sign
    if (!isDigitOrWhitespace(source, pos)) {
        // Invalid format: Not a valid integer number after minus sign
        pos.setIndex(initialIndex);
        return null;
    }
    
    // parse '/'
    c = parseNextCharacter(source, pos);
    if (c != '/') {
        // invalid '/' or missing numerator
        pos.setIndex(startIndex); // set back to the position of the minus sign
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
    // Ensure that the numerator is negative if the whole part or the denominator has a different sign than the numerator
    boolean isNegative = (w < 0 && n >= 0) || (n < 0 && d > 0);
    return new Fraction(isNegative ? -n : n, d);
}

private boolean isDigitOrWhitespace(String source, ParsePosition pos) {
    char c = source.charAt(pos.getIndex());
    if (Character.isDigit(c)) {
        pos.setIndex(pos.getIndex() + 1);
        return true;
    } else if (Character.isWhitespace(c)) {
        parseAndIgnoreWhitespace(source, pos);
        return true;
    }
    return false;
}