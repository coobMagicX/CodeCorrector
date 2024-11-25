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
            // Check for invalid scientific notation
            if (str.charAt(decPos - 1) == 'e' || str.charAt(decPos - 1) == 'E') {
                throw new NumberFormatException(str + " is not a valid number.");
            }
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
            // Check for invalid scientific notation
            if (str.charAt(expPos - 1) == 'e' || str.charAt(expPos - 1) == 'E') {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            exp = str.substring(expPos + 1);
        } else {
            exp = null;
        }
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
            boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            try {
                Float f = createFloat(str);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                Double d = createDouble(str);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }

            return createBigDecimal(str);

        }
    }
}

// Assuming these methods exist in your context:
private static Integer createInteger(String str) throws NumberFormatException {
    // Implementation of creating an integer from a string
}

private static Long createLong(String str) throws NumberFormatException {
    // Implementation of creating a long from a string
}

private static BigInteger createBigInteger(String str) throws NumberFormatException {
    // Implementation of creating a BigInteger from a string
}

private static Float createFloat(String str) {
    // Implementation of creating a float from a string
}

private static Double createDouble(String str) {
    // Implementation of creating a double from a string
}

private static BigDecimal createBigDecimal(String str) {
    // Implementation of creating a BigDecimal from a string
}

private static boolean isAllZeros(String str) {
    // Implementation to check if the string represents all zeros
}