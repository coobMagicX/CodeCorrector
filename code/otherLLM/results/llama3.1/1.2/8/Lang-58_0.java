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
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }
        mant = str.substring(0, decPos);
    } else {
        if (expPos > -1) {
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar)) {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length() - 1);
        } else {
            exp = null;
        }
        //Requesting a specific type..
        String numeric = str.substring(0, str.length() - 1);
        boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        switch (lastChar) {
            case 'l' :
            case 'L' :
                if (dec == null
                    && exp == null
                    && isDigits(numeric.substring(1))
                    && (numeric.charAt(0) == '-' || Character.isDigit(numeric.charAt(0)))) {
                    try {
                        return createLong(numeric);
                    } catch (NumberFormatException nfe) {
                        //Too big for a long
                    }
                    return createBigInteger(numeric);

                }
                if (isDigits(str.substring(1)) && str.startsWith("L")) {
                    try {
                        return createLong(str.substring(0, str.length() - 1));
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                    return createBigInteger(str);
                }
                throw new NumberFormatException(str + " is not a valid number.");
            case 'f' :
            case 'F' :
                try {
                    Float f = NumberUtils.createFloat(numeric);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        //If it's too big for a float or the float value = 0 and the string
                        // does not end with 'L'
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }

                if (isDigits(str.substring(1)) && str.startsWith("F")) {
                    try {
                        return createFloat(str.substring(0, str.length() - 1));
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                }
                throw new NumberFormatException(str + " is not a valid number.");
            case 'd' :
            case 'D' :
                try {
                    Double d = NumberUtils.createDouble(numeric);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        //If it's too big for a double or the double value = 0 and the string
                        // does not end with 'D'
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }

                if (isDigits(str.substring(1)) && str.startsWith("D")) {
                    try {
                        return createDouble(str.substring(0, str.length() - 1));
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                }
                throw new NumberFormatException(str + " is not a valid number.");
            case 'g' :
            case 'G' :
            default:
                try {
                    Double d = NumberUtils.createDouble(numeric);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }

                try {
                    Long l = createLong(str.substring(0, str.length() - 1));
                    if (!(l.isInfinite() || l == 0L)) {
                        return l;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }

                try {
                    Integer i = createInteger(str.substring(0, str.length() - 1));
                    if (!(i.isInfinite() || i == 0)) {
                        return i;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }

                return createBigInteger(str);

        }
    }
}