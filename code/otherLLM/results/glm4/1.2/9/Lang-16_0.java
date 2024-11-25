public static Number createNumber(String str) throws NumberFormatException {
    int expPos = str.indexOf('e') + 1;
    int decPos = str.indexOf('.');
    
    String mantissa, decimalPart, exponentPart;

    if (decPos > -1) {
        if (expPos > -1 && expPos < decPos) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
        mantissa = str.substring(0, decPos);
        decimalPart = str.substring(decPos + 1);
    } else {
        if (expPos > -1) {
            if (expPos > str.length()) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            mantissa = str.substring(0, expPos);
            exponentPart = str.substring(expPos);
        } else {
            mantissa = str;
            decimalPart = null;
            exponentPart = null;
        }
    }

    char lastChar = str.charAt(str.length() - 1);

    if (Character.isDigit(lastChar) || lastChar == '.') {
        // Handle case where no suffix is provided
        if (decimalPart != null && !decimalPart.isEmpty()) {
            try {
                return Double.parseDouble(mantissa + "." + decimalPart);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(str + " cannot be represented as a double.");
            }
        } else {
            try {
                return Integer.parseInt(mantissa);
            } catch (NumberFormatException e) {
                try {
                    return Long.parseLong(mantissa);
                } catch (NumberFormatException e2) {
                    return new BigInteger(mantissa);
                }
            }
        }
    } else {
        // Handle suffixes like 'l', 'f', etc.
        switch (lastChar) {
            case 'l':
            case 'L':
                try {
                    return Long.parseLong(mantissa);
                } catch (NumberFormatException e) {
                    return new BigInteger(mantissa);
                }
            case 'f':
            case 'F':
                try {
                    return Float.parseFloat(mantissa + "." + decimalPart);
                } catch (NumberFormatException e) {
                    return Double.parseDouble(mantissa + "." + decimalPart);
                }
            case 'd':
            case 'D':
                try {
                    return Double.parseDouble(mantissa + (decimalPart != null ? "." + decimalPart : ""));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException(str + " cannot be represented as a double.");
                }
            default:
                throw new NumberFormatException("Invalid number suffix: " + lastChar);
        }
    }
}