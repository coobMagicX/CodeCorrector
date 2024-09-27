public static Number createNumber(String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (str.trim().isEmpty()) {
        throw new NumberFormatException("A blank string is not a valid number");
    }  
    if (str.startsWith("--")) {
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
    int expPos = str.indexOf('e');
    if (expPos == -1) {
        expPos = str.indexOf('E');
    }
    
    if (decPos > -1) {
        if (expPos > -1) {
            if (expPos < decPos || expPos > str.length()) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }
        mant = str.substring(0, decPos);
    } else {
        if (expPos > -1) {
            if (expPos > str.length()) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar) && lastChar != '.') {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length() - 1);
        } else {
            exp = null;
        }
        //Requesting a specific type..
        String numeric = str.substring(0, str.length() - 1);
        boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        switch (lastChar) {
            case 'l':
            case 'L':
                if (dec == null && exp == null && (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
                    try {
                        return createLong(numeric);
                    } catch (NumberFormatException nfe) {
                        return createBigInteger(numeric);
                    }
                }
                throw new NumberFormatException(str + " is not a valid number.");
            case 'f':
            case 'F':
                try {
                    Float f = Float.parseFloat(numeric);
                    if (!(f.isInfinite() || (f == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                }
                // Intentional fall-through
            case 'd':
            case 'D':
                try {
                    Double d = Double.parseDouble(numeric);
                    if (!(d.isInfinite() || (d == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                }
                try {
                    return new BigDecimal(numeric);
                } catch (NumberFormatException e) {
                }
                throw new NumberFormatException(str + " is not a valid number.");
            default:
                throw new NumberFormatException(str + " is not a valid number.");
        }
    } else {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1);
        } else {
            exp = null;
        }
        if (dec == null && exp == null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException nfe) {
            }
            try {
                return Long.parseLong(str);
            } catch (NumberFormatException nfe) {
            }
            return new BigInteger(str);
        } else {
            boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            try {
                Float f = Float.parseFloat(str);
                if (!(f.isInfinite() || (f == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
            }
            try {
                Double d = Double.parseDouble(str);
                if (!(d.isInfinite() || (d == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
            }
            return new BigDecimal(str);
        }
    }
}

// Helper methods
private static boolean isDigits(String str) {
    return str.matches("\\d+");
}

private static boolean isAllZeros(String str) {
    return str != null && str.matches("0+");
}

private static Long createLong(String str) {
    return Long.parseLong(str);
}

private static Integer createInteger(String str) {
    return Integer.parseInt(str);
}

private static BigInteger createBigInteger(String str) {
    return new BigInteger(str);
}

private static BigDecimal createBigDecimal(String str) {
    return new BigDecimal(str);
}