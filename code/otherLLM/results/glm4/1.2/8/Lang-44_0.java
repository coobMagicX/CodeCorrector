public static Number createNumber(String val) throws NumberFormatException {
    if (val == null) {
        throw new NumberFormatException("null value is not a valid number.");
    }
    if (val.length() == 0) {
        throw new NumberFormatException("\"\" is not a valid number.");
    }
    if (val.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
        return null;
    }
    if (val.startsWith("0x") || val.startsWith("-0x")) {
        return createInteger(val);
    }   
    char lastChar = val.charAt(val.length() - 1);
    String mantissa;
    String decimalPart = "";
    String exponentPart = "";

    int decPos = val.indexOf('.');
    int expPos = Math.max(val.indexOf('e'), val.indexOf('E'));

    if (decPos > -1) {
        if (expPos > -1 && expPos < decPos) {
            throw new NumberFormatException(val + " is not a valid number.");
        }
        mantissa = val.substring(0, decPos);
        if (expPos > -1) {
            exponentPart = val.substring(decPos + 1, expPos).trim();
        } else if (decPos < val.length() - 1) {
            exponentPart = val.substring(decPos + 1).trim();
        }
    } else {
        mantissa = val;
    }

    boolean isAllZeros = isAllZeros(mantissa) && isAllZeros(exponentPart);

    switch (lastChar) {
        case 'l':
        case 'L':
            if (isDigits(mantissa)) {
                try {
                    return createLong(mantissa);
                } catch (NumberFormatException e) {
                    //Too big for a long, use BigInteger
                }
            }
            throw new NumberFormatException(val + " is not a valid number.");
        case 'f':
        case 'F':
            if (isDigits(mantissa)) {
                try {
                    Float f = NumberUtils.createFloat(mantissa);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
            }
            throw new NumberFormatException(val + " is not a valid number.");
        case 'd':
        case 'D':
            if (isDigits(mantissa)) {
                try {
                    Double d = NumberUtils.createDouble(mantissa);
                    if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException e) {
                    // empty catch
                }
            }
            throw new NumberFormatException(val + " is not a valid number.");
        default:
            if (isDigits(mantissa)) {
                try {
                    return createInteger(mantissa);
                } catch (NumberFormatException nfe) {
                    // empty catch
                }
                try {
                    return createLong(mantissa);
                } catch (NumberFormatException nfe) {
                    // empty catch
                }
                return createBigInteger(mantissa);
            } else if (decPos > -1 || expPos > -1) {
                if (isDigits(mantissa)) {
                    boolean allZeros = isAllZeros(mantissa) && isAllZeros(exponentPart);
                    try {
                        Float f = NumberUtils.createFloat(val);
                        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                            return f;
                        }
                    } catch (NumberFormatException nfe) {
                        // empty catch
                    }
                    try {
                        Double d = NumberUtils.createDouble(val);
                        if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                            return d;
                        }
                    } catch (NumberFormatException nfe) {
                        // empty catch
                    }
                }
            }
    }
    throw new NumberFormatException(val + " is not a valid number.");
}

public static boolean isAllZeros(String str) {
    for (char ch : str.toCharArray()) {
        if (ch != '0') {
            return false;
        }
    }
    return true;
}