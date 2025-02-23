public Complex parse(String source, ParsePosition pos) {
    int initialIndex = pos.getIndex();

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);

    // parse real
    Number re = parseNumber(source, getRealFormat(), pos);
    if (re == null) {
        pos.setIndex(initialIndex);
        return null;
    }

    // parse sign
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    int sign = 0;
    switch (c) {
        case 0:
            return new Complex(re.doubleValue(), 0.0);
        case '-':
            sign = -1;
            break;
        case '+':
            sign = 1;
            break;
        default:
            pos.setIndex(initialIndex);
            pos.setErrorIndex(startIndex);
            return null;
    }

    // parse whitespace
    parseAndIgnoreWhitespace(source, pos);

    // parse imaginary
    Number im = parseNumber(source, getRealFormat(), pos);
    if (im == null) {
        pos.setIndex(initialIndex);
        return null;
    }

    // parse imaginary character
    int n = getImaginaryCharacter().length();
    startIndex = pos.getIndex();
    int endIndex = startIndex + n;

    // Ensure that endIndex does not exceed source length
    if (endIndex > source.length() || !source.substring(startIndex, endIndex).equals(getImaginaryCharacter())) {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(startIndex);
        return null;
    }
    pos.setIndex(endIndex);

    return new Complex(re.doubleValue(), im.doubleValue() * sign);
}
