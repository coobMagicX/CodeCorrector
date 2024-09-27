import org.apache.commons.lang3.StringUtils;
import java.math.BigInteger;
import java.math.BigDecimal;

public class NumberUtils {

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
        for (final String pfx : hex_prefixes) {
            if (str.startsWith(pfx)) {
                pfxLen = pfx.length();
                break;
            }
        }
        if (pfxLen > 0) { // we have a hex number
            final int hexDigits = str.length() - pfxLen;
            if (hexDigits > 16) { // too many for Long
                return new BigInteger(str.substring(pfxLen), 16);
            }
            if (hexDigits > 8) { // too many for an Integer
                return Long.parseLong(str.substring(pfxLen), 16);
            }
            return Integer.parseInt(str.substring(pfxLen), 16);
        }
        final char lastChar = str.charAt(str.length() - 1);
        String mant;
        String dec;
        String exp;
        final int decPos = str.indexOf('.');
        final int expPos = Math.max(str.indexOf('e'), str.indexOf('E')); // corrected to use max

        int numDecimals = 0; // Check required precision
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
            numDecimals = dec.length();
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
            final String numeric = str.substring(0, str.length() - 1);
            final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            switch (lastChar) {
                case 'l':
                case 'L':
                    if (dec == null && exp == null && isDigits(numeric)) {
                        try {
                            return Long.parseLong(numeric);
                        } catch (final NumberFormatException nfe) {
                            return new BigInteger(numeric);
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
                    } catch (final NumberFormatException nfe) {
                        // Fall-through
                    }
                case 'd':
                case 'D':
                    try {
                        Double d = Double.parseDouble(numeric);
                        if (!(d.isInfinite() || (d == 0.0D && !allZeros))) {
                            return d;
                        }
                    } catch (final NumberFormatException nfe) {
                        // Fall-through
                    }
                    return new BigDecimal(numeric);
                default:
                    throw new NumberFormatException(str + " is not a valid number.");
            }
        }
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1);
        } else {
            exp = null;
        }
        if (dec == null && exp == null) {
            try {
                return Integer.parseInt(str);
            } catch (final NumberFormatException nfe) {
                try {
                    return Long.parseLong(str);
                } catch (final NumberFormatException nfe1) {
                    return new BigInteger(str);
                }
            }
        }
        try {
            return new BigDecimal(str);
        } catch (final NumberFormatException nfe) {
            throw new NumberFormatException(str + " is not a valid number.");
        }
    }

    private static boolean isAllZeros(String str) {
        return str != null && str.matches("^0+$");
    }

    private static boolean isDigits(String str) {
        return str.matches("^-?\\d+$");
    }
}