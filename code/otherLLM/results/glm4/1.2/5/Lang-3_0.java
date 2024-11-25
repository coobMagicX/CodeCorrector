import org.apache.commons.lang3.StringUtils;

public class NumberCreator {

    public static Number createNumber(final String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }

        final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};
        int pfxLen = 0;
        for (final String pfx : hex_prefixes) {
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
        final int decPos = str.indexOf('.');
        final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present

        if (decPos > -1) { // there is a decimal point
            if (expPos > -1) { // there is an exponent
                if (expPos < decPos || expPos > str.length()) {
                    throw new NumberFormatException(str + " is not a valid number.");
                }
                dec = str.substring(decPos + 1, expPos);
            } else {
                dec = str.substring(decPos + 1);
            }
            mant = str.substring(0, decPos);
        } else {
            mant = str;
            dec = null;
        }

        if (exp == null && dec == null) { // no decimal point and no exponent
            // Must be an Integer, Long, BigInteger
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
        }

        if (lastChar == 'd' || lastChar == 'D') {
            try {
                final Double d = NumberUtils.createDouble(mant);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && dec == null))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
        }

        if (lastChar == 'f' || lastChar == 'F') {
            try {
                final Float f = NumberUtils.createFloat(mant);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && dec == null))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
        }

        // Must be a Double, Float, BigDecimal
        try {
            final BigDecimal bd = new BigDecimal(mant);
            if (!bd.scale() > 0 && dec == null && !bd.isInfinite()) {
                return bd;
            }
        } catch (NumberFormatException e) {
            // ignore the bad number
        }

        return createBigDecimal(str);
    }

    private static Integer createInteger(String str) throws NumberFormatException {
        return Integer.parseInt(str);
    }

    private static Long createLong(String str) throws NumberFormatException {
        return Long.parseLong(str);
    }

    private static BigInteger createBigInteger(String str) throws NumberFormatException {
        return new BigInteger(str);
    }

    private static Float createFloat(String str) throws NumberFormatException {
        return Float.parseFloat(str);
    }

    private static Double createDouble(String str) throws NumberFormatException {
        return Double.parseDouble(str);
    }

    private static BigDecimal createBigDecimal(String str) throws NumberFormatException {
        return new BigDecimal(str);
    }
}