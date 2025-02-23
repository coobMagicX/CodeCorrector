public Complex parse(String source, ParsePosition pos) {
    int initialIndex = pos.getIndex();

    
    parseAndIgnoreWhitespace(source, pos);

    
    Number real = parseNumber(source, getRealFormat(), pos);
    if (real == null) {
        pos.setIndex(initialIndex);
        return null;
    }

    
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    int sign = 0;
    switch (c) {
        case 0 :
            return new Complex(real.doubleValue(), 0.0);
        case '-' :
            sign = -1;
            break;
        case '+' :
            sign = 1;
            break;
        default :
            pos.setIndex(initialIndex);
            pos.setErrorIndex(startIndex);
            return null;
    }

    
    parseAndIgnoreWhitespace(source, pos);

    
    Number imaginary = parseNumber(source, getRealFormat(), pos);
    if (imaginary == null) {
        pos.setIndex(initialIndex);
        return null;
    }

    
    int n = getImaginaryCharacter().length();
    startIndex = pos.getIndex();

    
    if (startIndex + n > source.length() || 
        !source.substring(startIndex, startIndex + n).equals(getImaginaryCharacter())) {
        pos.setIndex(initialIndex);
        pos.setErrorIndex(startIndex);
        return null;
    }
    pos.setIndex(startIndex + n);

    return new Complex(real.doubleValue(), imaginary.doubleValue() * sign);
}
