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
        // No valid whole number, restore index for other parsing
        pos.setIndex(initialIndex);  
        return null;
    }

    // Parse whitespace to check for a slash
    parseAndIgnoreWhitespace(source, pos);
    int slashIndex = pos.getIndex();
    char maybeSlash = parseNextCharacter(source, pos);
    if (maybeSlash != '/') {
        // If no slash, treat as whole number only if parsing stopped right after number
        pos.setIndex(initialIndex);
        return new Fraction(whole.intValue(), 1);
    }

    // Reset index to position right after white spaces following the whole number
    pos.setIndex(slashIndex);

    // Parse numerator after discovering a '/'
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(pos.getIndex());  // Set error index where it failed
        return null;
    }

    // Confirm proper '/' after numerator
    parseAndIgnoreWhitespace(source, pos);
    if (parseNextCharacter(source, pos) != '/') {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(pos.getIndex());
        return null;
    }

    // Parse denominator
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null) {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(pos.getIndex());
        return null;
    }

    // Construct the fraction properly considering signs
    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();
    if ((n < 0 || d < 0) && !(n < 0 && d < 0)) {
        // Both numerator and denominator must be either positive or negative if including minus sign
        pos.setIndex(initialIndex);
        return null;        
    }

    int sign = w < 0 ? -1 : 1;  // Determine sign based on the whole part
    w = Math.abs(w);  // Absolute value of whole

    // Return the new Fraction adjusting signs and computing the proper value
    return new Fraction((w * d + n) * sign, d);
}
