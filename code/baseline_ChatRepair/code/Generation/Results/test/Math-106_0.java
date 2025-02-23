public Fraction parse(String source, ParsePosition pos) {
    
    Fraction ret = super.parse(source, pos);
    if (ret != null) {
        return ret;
    }
    
    int initialIndex = pos.getIndex();

    
    parseAndIgnoreWhitespace(source, pos);

    
    Number whole = getWholeFormat().parse(source, pos);
    if (whole == null) {
        
        pos.setIndex(initialIndex);
        return null;
    }

    
    if(isIllegalSignAfterNumber(source, pos)) {
        pos.setIndex(initialIndex);
        return null;
    }

    
    parseAndIgnoreWhitespace(source, pos);
    
    
    Number num = getNumeratorFormat().parse(source, pos);
    if (num == null) {
        
        pos.setIndex(initialIndex);
        return null;
    }
    
    
    if(isIllegalSignAfterNumber(source, pos)) {
        pos.setIndex(initialIndex);
        return null;
    }

    
    int startIndex = pos.getIndex();
    char c = parseNextCharacter(source, pos);
    if (c != '/') {
        
        
        pos.setIndex(initialIndex);
        pos.setErrorIndex(startIndex);
        return null;
    }

    
    parseAndIgnoreWhitespace(source, pos);

    
    Number den = getDenominatorFormat().parse(source, pos);
    if (den == null || den.intValue() < 0) {
        
        pos.setIndex(initialIndex);
        return null;
    }

    int w = whole.intValue();
    int n = num.intValue();
    int d = den.intValue();

    if (w == 0 && d == 0) {
        
        return null;
    }

    
    return new Fraction(((Math.abs(w) * d) + n) * MathUtils.sign(w), d);
}


private boolean isIllegalSignAfterNumber(String source, ParsePosition pos) {
    int startIndex = pos.getIndex();
    parseAndIgnoreWhitespace(source, pos);
    if (pos.getIndex() < source.length() && source.charAt(pos.getIndex()) == '-') {
        pos.setIndex(startIndex);
        return true;
    }
    pos.setIndex(startIndex);
    return false;
}
