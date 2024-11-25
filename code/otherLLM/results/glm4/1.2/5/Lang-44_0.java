public static Number createNumber(String val) throws NumberFormatException {
    if (val == null) {
        throw new NumberFormatException("null is not a valid number.");
    }
    if (val.isEmpty()) {
        throw new NumberFormatException("\"\" is not a valid number.");
    }
    if (val.startsWith("--")) {
        return null; // This format is not allowed.
    }
    if (val.startsWith("0x") || val.startsWith("-0x")) {
        try {
            return createInteger(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(val + " is not a valid integer number.");
        }
    }

    char lastChar = val.charAt(val.length() - 1);
    String mant;
    String dec;
    int decPos = val.indexOf('.');
    int expPos = val.indexOf('e') + val.indexOf('E') + 1;

    if (decPos > -1) {
        if (expPos > -1 && expPos < decPos) {
            throw new NumberFormatException(val + " is not a valid number.");
        }
        mant = val.substring(0, decPos);
        if (expPos > -1) {
            dec = val.substring(decPos + 1, expPos);
        } else {
            dec = val.substring(decPos + 1);
        }
    } else {
        if (expPos > -1) {
            throw new NumberFormatException(val + " is not a valid number.");
        }
        mant = val;
        dec = null;
    }

    // Check for numeric suffixes
    boolean allZeros = isAllZeros(mant) && (dec == null || isAllZeros(dec));
    if (!Character.isDigit(lastChar)) {
        switch (lastChar) {
            case 'l':
            case 'L':
                if (dec == null && exp == null) {
                    try {
                        return createLong(mant);
                    } catch (NumberFormatException nfe) {
                        throw new NumberFormatException(mant + " is not a valid long number.");
                    }
                    return createBigInteger(mant);

                }
                throw new NumberFormatException(val + " is not a valid number with 'l' or 'L'.");
            case 'f':
            case 'F':
                try {
                    Float f = createFloat(mant);
                    if (dec == null || (f.floatValue() == 0.0F && allZeros)) {
                        return f;
                    }
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
                break;
            case 'd':
            case 'D':
                try {
                    Double d = createDouble(mant);
                    if (dec == null || (d.doubleValue() == 0.0D && allZeros)) {
                        return d;
                    }
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
                break;
            default:
                throw new NumberFormatException(val + " is not a valid number with numeric suffix.");
        }
    }

    if (dec == null && exp == null) {
        try {
            return createInteger(mant);
        } catch (NumberFormatException nfe) {
            // empty catch
        }
        try {
            return createLong(mant);
        } catch (NumberFormatException nfe) {
            // empty catch
        }
        return createBigInteger(mant);

    } else {
        boolean allZeros = isAllZeros(mant) && isAllZeros(dec);
        try {
            Float f = createFloat(val);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                return f;
            }
        } catch (NumberFormatException nfe) {
            // empty catch
        }
        try {
            Double d = createDouble(val);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                return d;
            }
        } catch (NumberFormatException nfe) {
            // empty catch
        }

        return createBigDecimal(val);

    }
}