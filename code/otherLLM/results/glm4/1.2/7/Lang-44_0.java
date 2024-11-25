public static Number createNumber(String val) throws NumberFormatException {
    if (val == null || val.trim().isEmpty()) {
        throw new NumberFormatException("Invalid input: " + val);
    }
    if (val.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
        // it accepts this as a legal value, but it does not appear 
        // to be in specification of class. OS X Java parses it to 
        // a wrong value.
        throw new NumberFormatException("Invalid input: " + val);
    }
    if (val.startsWith("0x") || val.startsWith("-0x")) {
        return createInteger(val);
    }   
    char lastChar = val.charAt(val.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = val.indexOf('.');
    int expPos = val.indexOf('e') + val.indexOf('E') + 1;

    if (decPos > -1) {
        mant = val.substring(0, decPos);
        if (expPos > -1) {
            if (expPos < decPos) {
                throw new NumberFormatException(val + " is not a valid number.");
            }
            dec = val.substring(decPos + 1, expPos);
            exp = val.substring(expPos + 1, val.length() - 1);
        } else {
            dec = val.substring(decPos + 1);
            exp = null;
        }
    } else {
        if (expPos > -1) {
            mant = val.substring(0, expPos);
            exp = val.substring(expPos + 1);
        } else {
            mant = val;
            exp = null;
        }
    }

    // Remove trailing characters that are not digits or a single 'l', 'L', 'f', 'F', 'd', 'D'
    while (lastChar != '\0' && !Character.isDigit(lastChar) && lastChar != 'l' && lastChar != 'L' && lastChar != 'f' && lastChar != 'F' && lastChar != 'd' && lastChar != 'D') {
        if (expPos > -1) {
            exp = val.substring(0, expPos);
        } else {
            mant = val;
        }
        lastChar = Character.toLowerCase(val.charAt(--val.length()));
    }

    if (!Character.isDigit(lastChar)) {
        if (lastChar == 'l' || lastChar == 'L') {
            if (dec != null || exp != null) {
                throw new NumberFormatException(val + " is not a valid long.");
            }
            try {
                return createLong(mant);
            } catch (NumberFormatException nfe) {
                return createBigInteger(mant);
            }
        } else if (lastChar == 'f' || lastChar == 'F') {
            // Check for infinity and zero without decimal part
            if ("inf".equals(mant) || "-inf".equals(mant)) {
                throw new NumberFormatException(val + " is not a valid float.");
            }
            try {
                Float f = createFloat(mant);
                if (f.isInfinite() || (f.floatValue() == 0.0F && !isAllZeros(dec))) {
                    throw new NumberFormatException(val + " is not a valid float.");
                }
                return f;
            } catch (NumberFormatException nfe) {
                // Handle as double
            }
        } else if (lastChar == 'd' || lastChar == 'D') {
            // Check for infinity and zero without decimal part
            if ("inf".equals(mant) || "-inf".equals(mant)) {
                throw new NumberFormatException(val + " is not a valid double.");
            }
            try {
                Double d = createDouble(mant);
                if (d.isInfinite() || (d.doubleValue() == 0.0D && !isAllZeros(dec))) {
                    throw new NumberFormatException(val + " is not a valid double.");
                }
                return d;
            } catch (NumberFormatException nfe) {
                // Handle as BigDecimal
            }
        } else {
            throw new NumberFormatException("Invalid type specifier: " + lastChar);
        }
    }

    if (dec != null || exp != null) {
        try {
            Float f = createFloat(mant + "." + dec);
            return f;
        } catch (NumberFormatException nfe) {
            try {
                Double d = createDouble(mant + "." + dec);
                return d;
            } catch (NumberFormatException nfe2) {
                return createBigDecimal(mant + "." + dec);
            }
        }
    }

    // If we reach here, it must be an integer type
    try {
        return createInteger(mant);
    } catch (NumberFormatException nfe) {
        try {
            return createLong(mant);
        } catch (NumberFormatException nfe2) {
            return createBigInteger(mant);
        }
    }
}