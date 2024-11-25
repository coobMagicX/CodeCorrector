public static Number createNumber(String val) throws NumberFormatException {
    if (val == null) {
        throw new NumberFormatException("null value is not a valid number.");
    }
    if (val.isEmpty()) {
        throw new NumberFormatException("Empty string is not a valid number.");
    }
    if (val.startsWith("--")) {
        return null; // this is protection for poorness in java.lang.BigDecimal
    }
    if (val.startsWith("0x") || val.startsWith("-0x")) {
        return createInteger(val);
    }   
    char lastChar = val.charAt(val.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = val.indexOf('.');
    int expPos = Math.max(val.indexOf('e'), val.indexOf('E')) + 1;

    if (decPos > -1) {
        if (expPos > -1 && expPos < decPos) {
            throw new NumberFormatException(val + " is not a valid number.");
        }
        dec = decPos == val.length() ? "" : val.substring(decPos + 1, expPos);
        mant = decPos == -1 ? val : val.substring(0, decPos);
    } else {
        if (expPos > -1) {
            mant = expPos == val.length() ? "" : val.substring(0, expPos);
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar)) {
        if (expPos > -1 && expPos < val.length() - 1) {
            exp = val.substring(expPos + 1, val.length() - 1);
        } else {
            exp = null;
        }
        //Requesting a specific type...
        String numeric = lastChar == 'l' || lastChar == 'L' ? val.replace("l", "").replace("L", "") : (lastChar == 'd' || lastChar == 'D' ? val.replace("d", "").replace("D", "") : val);
        boolean allZeros = isAllZeros(mant) && (exp == null || isAllZeros(exp));
        
        switch (lastChar) {
            case 'l':
            case 'L':
                return stringToInt(numeric, 0);
            case 'd':
            case 'D':
                return Double.parseDouble(numeric);
            default:
                if (!isAllZeros(mant)) {
                    try {
                        Float f = Float.parseFloat(numeric);
                        if (f.isInfinite() || (f == 0.0F && !allZeros)) {
                            break;
                        }
                        return f;
                    } catch (NumberFormatException e) {
                        // ignore the bad number
                    }
                }
            case 'f':
            case 'F': // Add support for float if needed
                try {
                    Float f = Float.parseFloat(numeric);
                    if (!(f.isInfinite() || (f == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
            default:
                break;
        }
    } else {
        try {
            Integer i = Integer.parseInt(val);
            if (!isAllZeros(mant)) {
                return i;
            }
        } catch (NumberFormatException nfe) {
            // empty catch
        }
        try {
            Long l = Long.parseLong(val);
            if (!isAllZeros(mant)) {
                return l;
            }
        } catch (NumberFormatException nfe) {
            // empty catch
        }
        return createBigInteger(val);
    }
}

private static boolean isAllZeros(String s) {
    if (s == null) {
        return true;
    }
    for (int i = s.length() - 1; i >= 0; i--) {
        if (s.charAt(i) != '0') {
            return false;
        }
    }
    return s.length() > 0;
}

public static int stringToInt(String str, int defaultValue) {
    try {
        return Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
        return defaultValue;
    }
}