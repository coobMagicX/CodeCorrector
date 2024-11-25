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
    if (expPos == -1) {
        expPos = str.length();
    }
    
    if (decPos > -1 && decPos < expPos) {
        if (expPos == str.length()) {
            exp = null;
        } else {
            exp = str.substring(expPos + 1);
        }
        dec = str.substring(decPos + 1, expPos);
        mant = str.substring(0, decPos);
    } else if (decPos == -1 && expPos != str.length()) {
        mant = str.substring(0, expPos);
        exp = null;
    } else {
        mant = str;
        dec = null;
    }
    
    if (!Character.isDigit(lastChar)) {
        String numeric = str.substring(0, str.length() - 1);
        boolean allZeros = isAllZeros(mant) && (exp == null || isAllZeros(exp));
        switch (lastChar) {
            case 'l':
            case 'L':
                if (dec == null && exp == null && isDigits(numeric.substring(1))) {
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
                    Float f = toFloat(numeric, 0.0F);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                //Fall through
            case 'd':
            case 'D':
                try {
                    Double d = toDouble(numeric, 0.0);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                //Fall through
            default:
                try {
                    Integer i = stringToInt(numeric, 0);
                    if (!(i.equals(Integer.MIN_VALUE) || (i == 0 && !allZeros))) {
                        return i;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                break;
        }
    } else {
        try {
            Integer i = stringToInt(mant, 0);
            if (!(i.equals(Integer.MIN_VALUE) || (i == 0 && dec != null && !isAllZeros(exp)))) {
                return i;
            }
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
        
        try {
            Long l = toLong(mant, 0L);
            if (!(l.equals(Long.MIN_VALUE) || (l == 0 && dec != null && !isAllZeros(exp)))) {
                return l;
            }
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }

        try {
            BigInteger bigInt = createBigInteger(mant);
            if (!(bigInt.equals(BigInteger.ZERO) && dec != null && !isAllZeros(exp))) {
                return bigInt;
            }
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
    }

    throw new NumberFormatException(str + " is not a valid number.");
}

private static boolean isAllZeros(String str) throws NumberFormatException {
    try {
        double d = toDouble(str, 0.0);
        return d == 0;
    } catch (NumberFormatException e) {
        return false;
    }
}