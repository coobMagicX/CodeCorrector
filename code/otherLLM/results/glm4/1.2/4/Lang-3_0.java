import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;

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
    final int expPos = str.indexOf('e') + str.indexOf('E'); // assumes both not present initially
    boolean hasExponent = false;

    if (expPos != -1) {
        // If 'e' or 'E' is found, it's an exponent and we should parse accordingly.
        dec = str.substring(0, expPos);
        exp = str.substring(expPos + 1).replace("e", "").replace("E", "");
        hasExponent = true;
    } else {
        // No exponent, the whole string is mantissa
        dec = str;
    }

    if (dec == null || dec.isEmpty()) {
        throw new NumberFormatException(str + " is not a valid number.");
    }

    final boolean allZeros = isAllZeros(dec);
    try {
        switch (lastChar) {
            case 'f':
            case 'F':
                final Float f = createFloat(dec);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
                break;

            case 'd':
            case 'D':
                final Double d = createDouble(dec);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
                break;

            case 'b':
            case 'B':
            case 'g':
            case 'G':
                // Assume we want BigDecimal if the last character is 'b', 'B', 'g', or 'G'
                final BigDecimal bd = new BigDecimal(dec);
                return bd;

            default:
                if (hasExponent) {
                    // Parse as BigDecimal directly
                    return new BigDecimal(dec + "e" + exp);
                } else {
                    try {
                        return createInteger(dec);
                    } catch (NumberFormatException e) {
                        // ignore the bad number
                    }
                    try {
                        return createLong(dec);
                    } catch (NumberFormatException e) {
                        // ignore the bad number
                    }
                    return createBigInteger(dec);
                }
        }
    } catch (NumberFormatException nfe) {
        // Ignore the bad number for now, it will be handled in the switch case or thrown later.
    }

    throw new NumberFormatException(str + " is not a valid number.");
}

private static boolean isAllZeros(String str) {
    return str.matches("^(-?0+)(\\.0*)?$");
}