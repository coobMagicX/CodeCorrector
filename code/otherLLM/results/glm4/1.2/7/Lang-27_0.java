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
        if (expPos > -1) {
            if (expPos < decPos) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            exp = str.substring(expPos + 1);
            mant = str.substring(0, decPos).replace(".", "");
        } else {
            exp = null;
            mant = str.substring(0, decPos).replace(".", "");
        }
    } else {
        if (str.endsWith("e") || str.endsWith("E")) {
            exp = str.substring(str.indexOf('e') + 1);
            mant = str.substring(0, str.lastIndexOf('e')).replace(".", "");
        } else {
            exp = null;
            mant = str.replace(".", "");
        }
    }

    if (lastChar == 'l' || lastChar == 'L' || lastChar == 'f' || lastChar == 'F' ||
        lastChar == 'd' || lastChar == 'D') {
        throw new NumberFormatException(str + " is not a valid number.");
    }

    boolean allZeros = isAllZeros(mant) && (exp == null || isAllZeros(exp));

    if (dec == null && exp == null) {
        // Must be an int, long, or bigint
        try {
            return createInteger(str);
        } catch (NumberFormatException nfe) {
            // Ignore the bad number
        }
        try {
            return createLong(str);
        } catch (NumberFormatException nfe) {
            // Ignore the bad number
        }
        return createBigInteger(str);

    } else if ((lastChar == 'f' || lastChar == 'F') && !allZeros) {
        return createFloat(mant + (exp != null ? "E" + exp : ""));
    } else if ((lastChar == 'd' || lastChar == 'D') && !allZeros) {
        return createDouble(mant + (exp != null ? "E" + exp : ""));
    } else {
        // Must be a float, double, or BigDecimal
        try {
            Float f = createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                return f;
            }
        } catch (NumberFormatException nfe) {
            // Ignore the bad number
        }
        try {
            Double d = createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                return d;
            }
        } catch (NumberFormatException nfe) {
            // Ignore the bad number
        }

        return createBigDecimal(str);

    }
}