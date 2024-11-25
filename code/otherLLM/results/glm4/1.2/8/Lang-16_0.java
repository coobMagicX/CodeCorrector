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
    int expPos = str.indexOf('e') + str.indexOf('E');

    if (decPos > -1) {
        expPos = Math.max(expPos, decPos); // Update expPos to be the last occurrence of 'e' or 'E'

        if (expPos == decPos || expPos >= str.length()) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
        dec = str.substring(decPos + 1, expPos);
    } else {
        if (expPos > -1) {
            if (expPos > str.length()) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }

    if (!Character.isDigit(lastChar) && lastChar != '.') {
        // Existing code for handling different numeric types (integers and decimals)
        // ...

        return convertToDesiredType(mant, dec, exp);
    } else {
        // User doesn't have a preference on the return type, so let's start
        // small and go from there...
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length());
        } else {
            exp = null;
        }
        if (dec == null && exp == null) {
            //Must be an int,long,bigint
            try {
                return createInteger(str);
            } catch (NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            try {
                return createLong(str);
            } catch (NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            return createBigInteger(str);

        } else {
            //Must be a float,double,BigDec
            boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            try {
                Float f = createFloat(str);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            try {
                Double d = createDouble(str);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }

            return createBigDecimal(str);

        }
    }
}

private static Number convertToDesiredType(String mant, String dec, String exp) throws NumberFormatException {
    try {
        if (exp == null || exp.isEmpty()) {
            // No exponent, treat as integer or decimal based on last character
            if (lastChar == 'd' || lastChar == 'D') {
                return createBigDecimal(mant);
            } else {
                return createInteger(mant);
            }
        } else {
            // Has an exponent, treat as a floating-point number
            if (lastChar == 'f' || lastChar == 'F') {
                return createFloat(mant + dec + exp);
            } else {
                return createDouble(mant + dec + exp);
            }
        }
    } catch (NumberFormatException e) {
        throw new NumberFormatException("Unable to convert '" + str + "' to a number.");
    }
}