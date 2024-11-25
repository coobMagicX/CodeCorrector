public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    // Need to deal with all possible hex prefixes here
    final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};
    int pfxLen = 0;
    for(final String pfx : hex_prefixes) {
        if (str.startsWith(pfx)) {
            pfxLen += pfx.length();
            break;
        }
    }
    if (pfxLen > 0) { // we have a hex number
        final int hexDigits = str.length() - pfxLen;
        if (hexDigits > 16) { // too many for Long
            return createBigInteger(str);
        }
        if (hexDigits > 8) { // too many for an int
            return createLong(str);
        }
        return createInteger(str);
    }
    final char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    final int decPos = str.indexOf('.');
    final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present
    // if both e and E are present, this is caught by the checks on expPos (which prevent IOOBE)
    // and the parsing which will detect if e or E appear in a number due to using the wrong offset

    int numDecimals = 0; // Check required precision (LANG-693)
    if (decPos > -1) { // there is a decimal point
        dec = str.substring(decPos + 1);
        mant = str.substring(0, decPos);
    } else {
        mant = str;
    }
    
    //User doesn't have a preference on the return type, so let's start
    //small and go from there...
    if (expPos > -1 && expPos < str.length() - 1) {
        exp = str.substring(expPos + 1);
    } else {
        exp = null;
    }
    
    //Must be a Float, Double, BigDecimal
    try {
            final Float f = createFloat(mant + (dec != null ? "." + dec : ""));
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && isAllZeros(mant) && exp == null))) {
                return f;
            }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }
    
    try {
            final Double d = createDouble(mant + (dec != null ? "." + dec : ""));
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && isAllZeros(mant) && exp == null))) {
                return d;
            }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    try {
            final BigDecimal bd = createBigDecimal(mant + (dec != null ? "." + dec : ""));
            if (!(bd.isInfinite() || (bd.doubleValue() == 0.0D && isAllZeros(mant) && exp == null))) {
                return bd;
            }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    //Must be an Integer, Long, Biginteger
    try {
        final int i = Integer.parseInt(str);
        return i;
    } catch (NumberFormatException e) {}

    try {
        final long l = Long.parseLong(str);
        return l;
    } catch (NumberFormatException e) {}

    try {
        final BigInteger bi = new BigInteger(str);
        return bi;
    } catch (NumberFormatException e) {}

    throw new NumberFormatException(str + " is not a valid number.");
}

private static boolean isAllZeros(String str) {
    for(char c : str.toCharArray()) {
        if(c != '0') {
            return false;
        }
    }
    return true;
}