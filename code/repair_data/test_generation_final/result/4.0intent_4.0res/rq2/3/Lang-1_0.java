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
            pfxLen = pfx.length();
            break;
        }
    }
    if (pfxLen > 0) { // we have a hex number
        final String hexPart = str.substring(pfxLen);
        BigInteger bigInt = new BigInteger(hexPart, 16);
        if (bigInt.bitLength() <= 31) { // fits in Integer
            return bigInt.intValue();
        } else if (bigInt.bitLength() <= 63) { // fits in Long
            return bigInt.longValue();
        } else { // fits in BigInteger
            return bigInt;
        }
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

        if (expPos > -1) { // there is an exponent
            if (expPos < decPos || expPos > str.length()) { // prevents double exponent causing IOOBE
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }
        mant = str.substring(0, decPos);
        numDecimals = dec.length(); // gets number of digits past the decimal to ensure no loss of precision for floating point numbers.
    } else {
        if (expPos > -1) {
            if (expPos > str.length()) { // prevents double exponent causing IOOBE
                throw new NumberFormatException(str + " is not a valid number.");
            }
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar) && lastChar != '.') {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length() - 1);
        } else {
            exp = null;
        }
        //Requesting a specific type..
        final String numeric = str.substring(0, str.length() - 1);
        final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        switch (lastChar) {
            case 'l' :
            case 'L' :
                if (dec == null && exp == null && isDigits(numeric)) {
                    try {
                        return createLong(numeric);
                    } catch (final NumberFormatException nfe) { // NOPMD
                        // Too big for a long
                    }
                    return createBigInteger(numeric);
                }
                throw new NumberFormatException(str + " is not a valid number.");
            case 'f' :
            case 'F' :
                try {
                    final Float f = NumberUtils.createFloat(numeric);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (final NumberFormatException nfe) { // NOPMD
                    // ignore the bad number
                }
                //$FALL-THROUGH$
            case 'd' :
            case 'D' :
                try {
                    final Double d = NumberUtils.createDouble(numeric);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (final NumberFormatException nfe) { // NOPMD
                    // ignore the bad number
                }
                try {
                    return createBigDecimal(numeric);
                } catch (final NumberFormatException e) { // NOPMD
                    // ignore the bad number
                }
                //$FALL-THROUGH$
            default :
                throw new NumberFormatException(str + " is not a valid number.");
        }
    }
    //User doesn't have a preference on the return type, so let's start
    //small and go from there...
    if (expPos > -1 && expPos < str.length() - 1) {
        exp = str.substring(expPos + 1, str.length());
    } else {
        exp = null;
    }
    if (dec == null && exp == null) { // no decimal point and no exponent
        //Must be an Integer, Long, Biginteger
        try {
            return createInteger(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createLong(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        return createBigInteger(str);
    }

    //Must be a Float, Double, BigDecimal
    final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
    try {
        if(numDecimals <= 7){// If number has 7 or fewer digits past the decimal point then make it a float
            final Float f = createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                return f;
            }
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }
    try {
        if(numDecimals <= 16){// If number has between 8 and 16 digits past the decimal point then make it a double
            final Double d = createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                return d;
            }
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }
    return createBigDecimal(str);
}