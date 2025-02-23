public Fraction parse(String source, ParsePosition pos) {
    // Initial position
    int initialIndex = pos.getIndex();

    // Parse whitespace to start
    parseAndIgnoreWhitespace(source, pos);

    // Try to parse improper fraction first
    Fraction ret = super.parse(source, pos);
    if (ret != null) {
        return ret;  // Correct improper fraction was parsed
    }
    
    // Reset index after improper fraction failure
    pos.setIndex(initialIndex);  

    // Parse whole number
    Number whole = getWholeFormat().parse(source, pos);
    if (whole == null) {
        pos.setIndex(initialIndex);
        return null;  // Failed to parse any number for whole
    }

    // Parse whitespace after whole number
    parseAndIgnoreWhitespace(source, pos);

    // Look for the slash since it's necessary for parsing as fraction
    int startNumeratorIndex = pos.getIndex();
    Number numerator = getNumeratorFormat().parse(source, pos);
    if (numerator == null) {
        // If no numerator, revert to whole number fraction
        pos.setIndex(initialIndex);
        return new Fraction(whole.intValue(), 1);
    }

    // Parse whitespace after numerator
    parseAndIgnoreWhitespace(source, pos);

    // Expecting a slash
    char slash = parseNextCharacter(source, pos);
    if (slash != '/') {
        pos.setIndex(initialIndex);
        return null;  // Not a valid fraction without a slash
    }

    // Parse whitespace after slash
    parseAndIgnoreWhitespace(source, pos);

    // Now parse the denominator
    Number denominator = getDenominatorFormat().parse(source, pos);
    if (denominator == null) {
        pos.setIndex(initialIndex);
        return null;  // No valid denominator parsed
    }

    // Compute the complete fraction
    int w = whole.intValue();
    int n = numerator.intValue();
    int d = denominator.intValue();

    // Check if the whole has a sign, adjust the entire fraction
    boolean negative = w < 0;
    int numeratorSign = (negative) ? -1 : 1;

    // Calculate final fraction considering positive or negative whole part
    n = Math.abs(w) * d + n;

    return new Fraction(numeratorSign * n, d);
}
