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
        if (expPos > -1 && expPos < decPos) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
        dec = str.substring(decPos + 1, expPos >= 0 ? expPos : str.length());
        mant = str.substring(0, decPos);
    } else {
        if (expPos > -1) {
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }

    //Requesting a specific type..
    String numeric = dec != null ? dec : mant;
    boolean allZeros = isAllZeros(mant) && (dec == null || isAllZeros(dec));

    switch (lastChar) {
        case 'l':
        case 'L':
            if (isDigits(numeric.substring(1))) {
                try {
                    return createLong(numeric);
                } catch (NumberFormatException nfe) {
                    //Too big for a long
                }
                return createBigInteger(numeric);
            }
            throw new NumberFormatException(str + " is not a valid number.");
        case 'f':
        case 'F':
            try {
                Float f = NumberUtils.createFloat(numeric);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            break; // Fall through to default case
        case 'd':
        case 'D':
            try {
                Double d = NumberUtils.createDouble(numeric);
                if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                return createBigDecimal(numeric);
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
            break; // Fall through to default case
        default:
            if (dec == null && exp == null) {
                //Must be an int,long,bigint
                try {
                    return createInteger(str);
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                try {
                    return createLong(str);
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                return createBigInteger(str);
            } else {
                //Must be a float,double,BigDec
                boolean allZeros = isAllZeros(mant) && (dec == null || isAllZeros(dec));
                try {
                    Float f = toFloat(str, 0.0f);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                try {
                    Double d = toFloat(str, 0.0);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }

                return createBigDecimal(str);
            }
    }

    throw new NumberFormatException(str + " is not a valid number.");
}