public static Number createNumber(String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }  
    if (str.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
        // it accepts this as a legal value, but it does not appear 
        // to be in specification of class. OS X Java parses it to 
        // a wrong value.
        return null;
    }
    if (str.startsWith("0x") || str.startsWith("-0x")) {
        return createInteger(str);
    }   
    char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = str.indexOf('.');
    int expPos = Math.max(str.indexOf('e'), str.indexOf('E'));
    
    if (decPos > -1) {
        if (expPos > -1 && (expPos < decPos || (str.charAt(expPos + 1) == 'e' && decPos < expPos))) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
        mant = str.substring(0, decPos);
        dec = str.substring(decPos + 1, expPos < 0 ? str.length() : expPos);
    } else {
        mant = str;
        dec = null;
    }

    if (expPos > -1 && (str.charAt(expPos + 1) == 'e' || str.charAt(expPos + 1) == 'E')) {
        exp = str.substring(expPos + 2, str.length());
    } else {
        exp = null;
    }

    //Requesting a specific type or autodetecting type...
    boolean allZeros = isAllZeros(mant) && (exp == null || isAllZeros(exp));
    switch (lastChar) {
        case 'l':
        case 'L':
            if (dec == null && exp == null && (isDigits(mant) || mant.equals("0"))) {
                try {
                    return createLong(mant);
                } catch (NumberFormatException nfe) {
                    //Too big for a long
                }
                return createBigInteger(mant);

            }
            throw new NumberFormatException(str + " is not a valid number.");
        case 'f':
        case 'F':
            if ((exp == null || isAllZeros(exp)) && (isDigits(mant) || mant.equals("0"))) {
                try {
                    Float f = createFloat(mant);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
            }
            break;
        case 'd':
        case 'D':
            if ((exp == null || isAllZeros(exp)) && (isDigits(mant) || mant.equals("0"))) {
                try {
                    Double d = createDouble(mant);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
            }
            break;
        default:
            if ((dec == null || isAllZeros(dec)) && (exp == null || isAllZeros(exp))) {
                try {
                    return createInteger(mant);
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
            }
            break;
    }

    throw new NumberFormatException(str + " is not a valid number.");
}