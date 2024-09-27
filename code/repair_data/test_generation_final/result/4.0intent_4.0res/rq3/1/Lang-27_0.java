import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberUtils {

    public static Number createNumber(String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
        str = str.trim();
        if (str.length() == 0) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        if (str.startsWith("--")) {
            return null;
        }
        if (str.startsWith("0x") || str.startsWith("-0x")) {
            return createInteger(str.substring(2), 16);
        }
        char lastChar = str.charAt(str.length() - 1);
        String mant;
        String dec;
        String exp;
        int decPos = str.indexOf('.');
        int expPos = Math.max(str.indexOf('e'), str.indexOf('E'));

        if (decPos > -1) {
            if (expPos > -1) {
                dec = str.substring(decPos + 1, expPos);
            } else {
                dec = str.substring(decPos + 1);
            }
            mant = str.substring(0, decPos);
        } else {
            if (expPos > -1) {
                mant = str.substring(0, expPos);
            } else {
                mant = str;
            }
            dec = null;
        }

        if (expPos > -1) {
            exp = str.substring(expPos + 1);
        } else {
            exp = null;
        }

        if (!Character.isDigit(lastChar) && lastChar != '.') {
            str = str.substring(0, str.length() - 1);
        }

        if (dec == null && exp == null) {
            try {
                return new Integer(str);
            } catch (NumberFormatException nfe) {
                try {
                    return new Long(str);
                } catch (NumberFormatException nfe2) {
                    return new BigInteger(str);
                }
            }
        } else {
            try {
                return new BigDecimal(str);
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
        }
    }

    private static Integer createInteger(String str, int radix) {
        return Integer.parseInt(str, radix);
    }

    public static void main(String[] args) {
        try {
            System.out.println(createNumber("123"));
            System.out.println(createNumber("123.456"));
            System.out.println(createNumber("123e10"));
            System.out.println(createNumber("0x1F"));
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }
}