public static Number createNumber(String val) throws NumberFormatException {
    if (val == null) {
        return null;
    }
    if (val.length() == 0) {
        throw new NumberFormatException("\"\" is not a valid number.");
    }
    if (val.startsWith("--")) {
        return null;
    }
    if (val.startsWith("0x") || val.startsWith("-0x")) {
        return createInteger(val);
    }
    char lastChar = val.charAt(val.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = val.indexOf('.');
    int expPos = Math.max(val.indexOf('e'), val.indexOf('E')); // Corrected to find the maximum index of 'e' or 'E'

    if (decPos > -1) {
        if (expPos > -1) {
            if (expPos < decPos) {
                throw new NumberFormatException(val + " is not a valid number.");
            }
            dec = val.substring(decPos + 1, expPos);
        } else {
            dec = val.substring(decPos + 1);
        }
        mant = val.substring(0, decPos);
    } else {
        if (expPos > -1) {
            mant = val.substring(0, expPos);
        } else {
            mant = val;
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar)) {
        if (expPos > -1 && expPos < val.length() - 1) {
            exp = val.substring(expPos + 1, val.length() - 1);
        } else {
            exp = null;
        }
        String numeric = val.substring(0, val.length() - 1);
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
                throw new NumberFormatException(val + " is not a valid number.");
            case 'f':
            case 'F':
                try {
                    Float f = Float.parseFloat(numeric);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
                // Fall through
            case 'd':
            case 'D':
                try {
                    Double d = Double.parseDouble(numeric);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // empty catch
                }
                try {
                    return new BigDecimal(numeric);
                } catch (NumberFormatException e) {
                    // empty catch
                }
                throw new NumberFormatException(val + " is not a valid number.");
            default:
                throw new NumberFormatException(val + " is not a valid number.");
        }
    } else {
        if (expPos > -1 && expPos < val.length() - 1) {
            exp = val.substring(expPos + 1, val.length());
        } else {
            exp = null;
        }
        if (dec == null && exp == null) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException nfe) {
                try {
                    return Long.parseLong(val);
                } catch (NumberFormatException nfe2) {
                    return new BigInteger(val);
                }
            }
        } else {
            try {
                Float f = Float.parseFloat(val);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                try {
                    Double d = Double.parseDouble(val);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe2) {
                    return new BigDecimal(val);
                }
            }
        }
    }
}