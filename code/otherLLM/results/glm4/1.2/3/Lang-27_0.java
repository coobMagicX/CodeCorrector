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
    boolean isLongPrefixed = false, isFloatPrefixed = false, isDoublePrefixed = false;

    // Handle numeric suffixes (l/L/f/F/d/D)
    if (lastChar == 'l' || lastChar == 'L') {
        isLongPrefixed = true;
        str = str.substring(0, str.length() - 1);
    } else if (lastChar == 'f' || lastChar == 'F') {
        isFloatPrefixed = true;
        str = str.substring(0, str.length() - 1);
    } else if (lastChar == 'd' || lastChar == 'D') {
        isDoublePrefixed = true;
        str = str.substring(0, str.length() - 1);
    }

    // Split into mantissa and exponent
    int expIndex = str.indexOf('e');
    if (expIndex != -1) {
        mant = str.substring(0, expIndex);
        exp = str.substring(expIndex + 1);
    } else {
        mant = str;
        exp = null;
    }

    // Check for all zeros
    boolean allZeros = isAllZeros(mant);

    if (isLongPrefixed) {
        try {
            return createLong(mant);
        } catch (NumberFormatException nfe) {
            //Too big for a long
        }
        return createBigInteger(mant);
    } else if (isFloatPrefixed || isDoublePrefixed) {
        double value;
        if (exp != null) {
            value = Double.parseDouble(mant + "e" + exp);
        } else {
            value = Double.parseDouble(mant);
        }
        if (!Double.isInfinite(value) && (value == 0.0 || !allZeros)) {
            return new Double(value);
        }
    }

    // Determine the type based on the last character
    switch (lastChar) {
        case 'f' :
        case 'F' :
            try {
                Float f = createFloat(mant);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            break;
        case 'd' :
        case 'D' :
            try {
                Double d = createDouble(mant);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            break;
        case 'l' :
        case 'L' :
        case 'i' :
        case 'I' :
            try {
                Long l = createLong(mant);
                if (!allZeros) {
                    return l;
                }
            } catch (NumberFormatException nfe) {
                //Too big for a long
            }
            break;
        case 'b' :
        case 'B' :
            return createBigInteger(mant);

        default:
            throw new NumberFormatException(str + " is not a valid number.");
    }

    // If none of the above, try to create BigDecimal
    return createBigDecimal(mant);
}

// Assume the existence of these methods as per your instructions
private static boolean isAllZeros(String numStr) {
    for (char c : numStr.toCharArray()) {
        if (c != '0') {
            return false;
        }
    }
    return true;
}

private static boolean isDigits(String str) {
    for (int i = 0; i < str.length(); i++) {
        if (!Character.isDigit(str.charAt(i))) {
            return false;
        }
    }
    return true;

    // Other methods like createInteger, createLong, createBigInteger,
    // createFloat, and createDouble should be implemented as per your requirements.
}