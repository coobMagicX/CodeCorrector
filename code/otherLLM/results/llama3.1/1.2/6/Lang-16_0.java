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
    if (str.startsWith("0x") || str.equalsIgnoreCase("0X")) {
        try {
            return Long.parseLong(str.substring(2), 16);
        } catch (NumberFormatException e) {
            // ignore and continue with other parsing logic
        }
    }
    if (str.startsWith("-0x") || str.equalsIgnoreCase("-0X")) {
        try {
            return -Long.parseLong(str.substring(3, str.length()), 16);
        } catch (NumberFormatException e) {
            // ignore and continue with other parsing logic
        }
    }
    char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = str.indexOf('.');
    int expPos = str.indexOf('e') + str.indexOf('E') + 1;

    if (decPos > -1) {

        if (expPos > -1) {
            if (expPos < decPos || expPos > str.length()) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }

    } else {

        if (expPos > -1) {
            if (expPos < str.length()) {
                exp = str.substring(expPos + 1);
            } else {
                exp = null;
            }
        } else {
            exp = null;
        }
    }

    if (dec == null && exp == null) {
        //Must be an int,long,bigint
        try {
            return createLong(str);
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        return createBigInteger(str);

    } else {
        //Must be a float,double,BigDec
        boolean allZeros = isAllZeros(mant, dec, exp);
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

        try {
            return toFloat(str, 0);
        } catch (NumberFormatException e) {
            // ignore the bad number
        }

        return createBigDecimal(str);

    }
}

private static boolean isAllZeros(String mant, String dec, String exp) {
    if (mant == null && dec == null && exp == null) {
        return true;
    }
    int i = 0;
    if (mant != null) {
        while (i < mant.length() && Character.isDigit(mant.charAt(i))) {
            i++;
        }
    }
    if (dec != null) {
        while (i < dec.length() && Character.isDigit(dec.charAt(i))) {
            i++;
        }
    }
    if (exp != null) {
        while (i < exp.length() && Character.isDigit(exp.charAt(i))) {
            i++;
        }
    }

    return mant == null || dec == null || exp == null || i >= Math.max(mant.length(), dec.length());
}
