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
    String mantissa;
    String decimalPart;
    String exponentPart;
    int decimalPosition = str.indexOf('.');
    int exponentPosition = str.indexOf('e') + str.indexOf('E') + 1;

    if (decimalPosition > -1) {
        if (exponentPosition > -1) {
            if (exponentPosition < decimalPosition) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            exponentPart = str.substring(exponentPosition + 1);
        } else {
            exponentPart = null;
        }
        mantissa = str.substring(0, decimalPosition);
    } else {
        if (exponentPosition > -1) {
            mantissa = str.substring(0, exponentPosition);
        } else {
            mantissa = str;
        }
        decimalPart = null;
    }
    String numericValue = lastChar == 'L' || lastChar == 'l'
                           ? mantissa + "L"
                           : mantissa;

    boolean allZeros = isAllZeros(mantissa) && (exponentPart != null ? isAllZeros(exponentPart) : true);
    switch (lastChar) {
        case 'l' :
        case 'L' :
            try {
                return createLong(numericValue);
            } catch (NumberFormatException nfe) {
                //Too big for a long
                return createBigInteger(mantissa + exponentPart != null ? mantissa + "E" + exponentPart : mantissa);
            }
        case 'f' :
        case 'F' :
            try {
                Float f = NumberUtils.createFloat(numericValue);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            //Fall through to double check
        case 'd' :
        case 'D' :
            try {
                Double d = NumberUtils.createDouble(numericValue);
                if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                return createBigDecimal(mantissa + exponentPart != null ? mantissa + "E" + exponentPart : mantissa);
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
            //Fall through
        default :
            throw new NumberFormatException(str + " is not a valid number.");
    }
}