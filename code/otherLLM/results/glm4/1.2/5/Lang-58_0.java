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
    int expPos = str.indexOf('e') + str.indexOf('E') + 1;

    if (decPos > -1) {
        // Handle decimal numbers
        if (expPos > -1 && expPos < decPos) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
        mant = str.substring(0, decPos);
        dec = str.substring(decPos + 1, Math.min(expPos, str.length()));
        exp = (expPos == -1 || expPos > decPos) ? null : str.substring(expPos + 1).trim();
    } else {
        // Handle integer-like and floating-point numbers
        if (expPos > -1) {
            mant = str.substring(0, expPos);
            exp = str.substring(expPos + 1).trim();
        } else {
            mant = str;
            exp = null;
        }
    }

    boolean allZeros = isAllZeros(mant) && (exp == null || isAllZeros(exp));
    if (!Character.isDigit(lastChar)) {
        // Determine the type of number and create it accordingly
        switch (lastChar) {
            case 'l':
            case 'L':
                if (dec == null && exp == null && isDigits(mant.substring(1))) {
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
                if (exp == null && !isAllZeros(mant)) {
                    try {
                        return createFloat(mant);
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                }
                break;
            case 'd':
            case 'D':
                if (exp == null && !isAllZeros(mant)) {
                    try {
                        return createDouble(mant);
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                }
                break;
            default:
                if (dec == null && exp == null && isDigits(str)) {
                    try {
                        return createInteger(str);
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                }
                throw new NumberFormatException(str + " is not a valid number.");
        }
    }

    // Fallback to BigDecimal for other cases
    if (exp == null || !isAllZeros(exp)) {
        try {
            return createBigDecimal(mant + ((dec != null && dec.length() > 0) ? ("." + dec) : ""));
        } catch (NumberFormatException e) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
    }

    // If all else fails, throw an exception
    throw new NumberFormatException(str + " is not a valid number.");
}

// Methods for creating integers, longs, bigIntegers, floats, doubles, and bigDecimals are assumed to be available.