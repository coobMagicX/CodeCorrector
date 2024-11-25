public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }

    // Handle hex numbers first
    final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};
    int pfxLen = 0;
    for(final String pfx : hex_prefixes) {
        if (str.startsWith(pfx)) {
            pfxLen = pfx.length();
            break;
        }
    }

    // Extract the numeric part without the prefix
    final String numericPart = str.substring(pfxLen);

    // If it's a hex number, attempt to convert it to a BigDecimal
    if (!numericPart.isEmpty()) {
        try {
            return createBigDecimal(numericPart);
        } catch (NumberFormatException e) {
            // Not a valid BigDecimal, continue with other types
        }
    }

    // Now handle the rest of the cases for decimal numbers

    if (str.endsWith("f") || str.endsWith("F")) {
        try {
            final Float f = NumberUtils.createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F))) {
                return f;
            }
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
    }

    if (str.endsWith("d") || str.endsWith("D")) {
        try {
            final Double d = NumberUtils.createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D))) {
                return d;
            }
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
    }

    // If it's not a float or double, default to BigDecimal
    return createBigDecimal(str);
}

public static BigDecimal createBigDecimal(final String str) {
    if (str == null || StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    return new BigDecimal(str);
}