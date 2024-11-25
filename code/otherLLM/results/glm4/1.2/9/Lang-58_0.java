public static Number createNumber(String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    if (str.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
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
            if (expPos < decPos) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
            mant = str.substring(0, decPos);
        } else {
            dec = str.substring(decPos + 1);
            mant = str.substring(0, decPos);
        }
    } else {
        if (expPos > -1) {
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }

    if (!Character.isDigit(lastChar)) {
        if (lastChar == 'l' || lastChar == 'L') {
            String numericType = str.substring(0, str.length() - 1).trim();
            if (isDigits(numericType) && (numericType.charAt(0) == '-' || Character.isDigit(numericType.charAt(0)))) {
                try {
                    return createLong(numericType);
                } catch (NumberFormatException nfe) {
                    //Too big for a long
                }
                return createBigInteger(numericType);
            }
        }
        if (lastChar == 'f' || lastChar == 'F') {
            try {
                Float f = NumberUtils.createFloat(mant);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && dec != null))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
        }
        if (lastChar == 'd' || lastChar == 'D') { // Assuming 'd' stands for double
            try {
                Double d = NumberUtils.createDouble(mant);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && dec != null))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
        }

        throw new NumberFormatException(str + " is not a valid number.");
    } else {
        if (dec == null && exp == null) {
            try {
                return createInteger(mant);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                return createLong(mant);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            return createBigInteger(mant);
        } else {
            boolean allZeros = isAllZeros(toDoubleArray(mant)) && isAllZeros(toDoubleArray(dec));
            try {
                Float f = NumberUtils.createFloat(mant + (dec != null ? "." + dec : ""));
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }

            try {
                Double d = NumberUtils.createDouble(mant + (dec != null ? "." + dec : ""));
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }

            return createBigDecimal(mant + (dec != null ? "." + dec : ""));
        }
    }
}

private static double[] toDoubleArray(String str) {
    if (str == null || str.isEmpty()) {
        return new double[0];
    }
    double value;
    try {
        value = Double.parseDouble(str);
    } catch (NumberFormatException e) {
        return new double[0];
    }
    return new double[]{value};
}