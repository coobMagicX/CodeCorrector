public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    // Need to deal with all possible hex prefixes here
    final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};
    int pfxLen = 0;
    for(final String pfx : hex_prefixes) {
        if (str.startsWith(pfx)) {
            pfxLen += pfx.length();
            break;
        }
    }
    if (pfxLen > 0) { // we have a hex number
        final int hexDigits = str.length() - pfxLen;
        if (hexDigits > 16) { // too many for Long
            return createBigInteger(str);
        }
        if (hexDigits > 8) { // too many for an int
            return createLong(str);
        }
        return createInteger(str);
    }
    final char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    boolean isFiniteFloat = false;
    boolean isFiniteDouble = false;

    final int exponentIndex = str.indexOf('e');
    if (exponentIndex != -1) {
        // Check for 'e' and '+' or '-' sign
        final char eSign = str.charAt(exponentIndex + 1);
        int exponentValue = Integer.parseInt(str.substring(exponentIndex + 2));
        String tempStr = str.substring(0, exponentIndex);

        if (tempStr.contains(".")) {
            // Split string into mantissa and fractional part
            String[] parts = tempStr.split("\\.");
            mant = parts[0];
            dec = parts.length > 1 ? "." + parts[1] : "";
        } else {
            mant = str.substring(0, exponentIndex);
            exp = str.substring(exponentIndex + 2);
        }

        if (eSign == '+') {
            // Positive exponent
            exponentValue += mant.length() - 1;
            while (exponentValue-- > 0) {
                mant += '0';
            }
        } else {
            // Negative exponent
            exponentValue = Math.abs(exponentValue);
            int decimalPlace = mant.length();
            if (dec != null && !dec.isEmpty()) {
                decimalPlace += dec.length() - 1;
            }

            while (exponentValue-- > 0) {
                mant = '0' + mant;
                if (dec != null && !dec.isEmpty()) {
                    dec = "0" + dec;
                }
            }

            if (!exp.isEmpty()) {
                exp += exponentValue;
            } else {
                exp = String.valueOf(exponentValue);
            }
        }
    } else {
        // No 'e' in the string
        if (str.contains(".")) {
            // Split string into mantissa and fractional part
            String[] parts = str.split("\\.");
            mant = parts[0];
            dec = parts.length > 1 ? "." + parts[1] : "";
        } else {
            mant = str;
            exp = null;
        }
    }

    if (dec != null && !dec.isEmpty()) {
        // Check for finite float and double
        final String tempStr = mant + '.' + dec;
        try {
            final Float f = createFloat(tempStr);
            isFiniteFloat = !(f.isInfinite() || (f.floatValue() == 0.0F));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (!isFiniteFloat && !exp.isEmpty()) {
        try {
            final Double d = createDouble(str);
            isFiniteDouble = !(d.isInfinite() || (d.doubleValue() == 0.0D));
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    if (isFiniteFloat) {
        try {
            return createFloat(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    } else if (isFiniteDouble) {
        try {
            return createDouble(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    return createBigDecimal(str);
}