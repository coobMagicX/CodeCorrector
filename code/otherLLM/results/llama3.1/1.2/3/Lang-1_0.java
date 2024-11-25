public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number.");
    }

    final boolean isHex = str.startsWith("0x");
    if (isHex) {
        try {
            return createBigInteger(str.substring(2));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createBigDecimal(str.substring(2));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    final boolean isBinary = str.startsWith("0b");
    if (isBinary) {
        try {
            return createBigInteger(str.substring(2));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createBigDecimal(str.substring(2));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (str.matches("[0-9]+")) {
        try {
            return createLong(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createBigInteger(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (str.matches("0x[0-9a-fA-F]+")) {
        try {
            return createBigInteger(str.substring(2));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createBigDecimal(str.substring(2));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (str.matches("0b[01]+")) {
        try {
            return createBigInteger(str.substring(2));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createBigDecimal(str.substring(2));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    final boolean allZeros = isAllZeros(str);
    if (str.endsWith("l") || str.endsWith("L")) {
        try {
            return createLong(str.substring(0, str.length() - 1));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        return createBigInteger(str);
    }

    if (str.endsWith("f") || str.endsWith("F")) {
        try {
            final Float f = createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                return f;
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (str.endsWith("d") || str.endsWith("D")) {
        try {
            final Double d = createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                return d;
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    try {
        return createInteger(str);
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    final boolean hasDecimalPoint = str.contains(".");
    if (hasDecimalPoint && !str.endsWith("l") && !str.endsWith("L")
            && !str.endsWith("f") && !str.endsWith("F")) {
        try {
            return createFloat(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (hasDecimalPoint && str.endsWith("d")
            || hasDecimalPoint && str.endsWith("D")) {
        try {
            return createDouble(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    final boolean hasExponent = str.contains("e");
    if (hasExponent && !str.endsWith("f") && !str.endsWith("F")
            && !str.endsWith("d") && !str.endsWith("D")) {
        try {
            return createBigDecimal(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (!Character.isDigit(str.charAt(0))) {
        final String numeric = str.substring(1, str.length());
        try {
            return createBigInteger(numeric);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createBigDecimal(numeric);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    try {
        return createBigInteger(str);
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    if (allZeros && !str.endsWith("l") && !str.endsWith("L")
            && !str.endsWith("f") && !str.endsWith("F")) {
        try {
            return createInteger(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (!Character.isDigit(str.charAt(0))) {
        final String numeric = str.substring(1, str.length());
        try {
            return createLong(numeric);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createBigInteger(numeric);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (!Character.isDigit(str.charAt(0))) {
        final String numeric = str.substring(1, str.length());
        try {
            return createBigDecimal(numeric);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    try {
        return createBigDecimal(str);
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    return null;
}

private boolean isAllZeros(final String str) {
    for (int i = 0; i < str.length(); i++) {
        if (!Character.isDigit(str.charAt(i))) {
            return false;
        }
    }
    return true;
}