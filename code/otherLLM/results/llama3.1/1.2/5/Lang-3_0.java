public static Number createNumber(final String str) throws NumberFormatException {
    // ... (rest of the code remains the same)

    int numDecimals = 0; // Check required precision (LANG-693)
    if (decPos > -1) { // there is a decimal point

        if (expPos > -1) { // there is an exponent
            if (expPos < decPos || expPos > str.length()) { // prevents double exponent causing IOOBE
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }
        mant = str.substring(0, decPos);
        numDecimals = dec.length(); // gets number of digits past the decimal to ensure no loss of precision for floating point numbers.
    } else {
        if (expPos > -1) {
            if (expPos > str.length()) { // prevents double exponent causing IOOBE
                throw new NumberFormatException(str + " is not a valid number.");
            }
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }

    if (!Character.isDigit(lastChar) && lastChar != '.') {
        // ... (rest of the code remains the same)

        try {
            final Float f = createFloat(mant + dec);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                return f;
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            final Double d = createDouble(mant + dec);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                return d;
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

        try {
            return createBigDecimal(mant + dec);
        } catch (final NumberFormatException e) { // NOPMD
            // ignore the bad number
        }
    }

    // ... (rest of the code remains the same)
}