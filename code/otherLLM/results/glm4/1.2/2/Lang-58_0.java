import java.math.BigDecimal;

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
    int exponentPosition = str.indexOf('e') + str.indexOf('E');

    if (decimalPosition > -1) {
        if (exponentPosition > -1 && exponentPosition < decimalPosition) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
        mantissa = str.substring(0, decimalPosition);
        if (exponentPosition > -1) {
            exponentPart = str.substring(decimalPosition + 1, exponentPosition).trim();
        } else {
            exponentPart = str.substring(decimalPosition + 1).trim();
        }
    } else {
        if (exponentPosition > -1) {
            mantissa = str.substring(0, exponentPosition);
        } else {
            mantissa = str;
        }
        decimalPart = null;
        exponentPart = str.substring(exponentPosition + 1).trim();
    }

    if (!Character.isDigit(lastChar)) {
        if (lastChar == 'l' || lastChar == 'L') {
            if (decimalPart == null && exponentPart == null
                && isDigits(mantissa.replace("-", ""))) {
                try {
                    return createLong(mantissa);
                } catch (NumberFormatException nfe) {
                    //Too big for a long
                }
                return new BigDecimal(mantissa);

            }
        }
    }

    if (exponentPart != null) {
        exponentPart = exponentPart.replaceAll("[eE]", "");
    }

    try {
        if (lastChar == 'l' || lastChar == 'L') {
            return Long.parseLong(mantissa + exponentPart);
        } else if (lastChar == 'f' || lastChar == 'F' || Character.isDigit(lastChar)) {
            return Double.parseDouble(mantissa + decimalPart + exponentPart);
        } else if (lastChar == 'd' || lastChar == 'D') {
            return new BigDecimal(mantissa + decimalPart + exponentPart);
        }
    } catch (NumberFormatException e) {
        throw new NumberFormatException(str + " is not a valid number.");
    }

    // Default case, assuming the string is an integer
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
    return new BigDecimal(str);
}