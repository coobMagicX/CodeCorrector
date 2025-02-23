public Fraction parse(String source, ParsePosition pos) {
    // Try to parse improper fraction
    Fraction ret = super.parse(source, pos);
    if (ret != null) {
        return ret;
    }

    int initialIndex = pos.getIndex();

    // Parse whitespace before whole number
    parseAndIgnoreWhitespace(source, pos);

    // Parse whole
    Number whole = getWholeFormat().parse(source, pos);
    if (whole == null) {
        // No valid whole number, not an error
        pos.setIndex(initialIndex);  // Reset parse position
        return null;
    }

    // Validate character immediately following the whole number
    int positionAfterWhole = pos.getIndex();
    parseAndIgnoreWhitespace(source, pos);
    int slashIndex = pos.getIndex();
    char maybeSlash = parseNextCharacter(source, pos);
    if (maybeSlash != '/') {
        pos.setIndex(initialIndex);  // Reset as no valid '/' following whole
        return new Fraction(whole.intValue(), 1);
    }

    // Reset index to position right after whole number
    pos.setIndex(slashIndex);

    // Parse numerator
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(pos.getIndex());  // Set error index where it failed
        return null;
    }

    // Parse slash
    parseAndIgnoreWhitespace(source, pos);
    char c = parseNextCharacter(source, pos);
    if (c != '/') {
        // Error if no '/' after numerator
        pos.setIndex(initialIndex);
        pos.setErrorIndex(pos.getIndex());
        return null;
    }

    // Parse denominator
    parseAndIgnoreWhitespace(source, pos);
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null) {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(pos.getIndex());
        return null;
    }

    // Ensure proper signs
    if (num.intValue() < 0 || den.intValue() < 0) {
        // Minus signs in num or den are not accepted in this context
        pos.setIndex(initialIndex);
        pos.setErrorIndex(positionAfterWhole);
        return null;
    }

    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();

    // Compute the fraction considering the whole part
    return new Fraction(((Math.abs(w) * d) + n) * Math.signum(w), d);
}
