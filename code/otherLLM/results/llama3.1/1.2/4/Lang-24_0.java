import java.util.EmptyException;

public class NumberUtils {

    /**
     * Checks if the input string is a valid number.
     *
     * @param str The input string to validate.
     * @return True if the string represents a numeric value; false otherwise.
     */
    public static boolean isNumber(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        char[] chars = str.toCharArray();
        int sz = chars.length;

        // Flags to track whether an exponent, decimal point or sign has been encountered
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false; // True if the number ends with 'E'

        boolean foundDigit = false;

        // Handle any leading signs
        int start = (chars[0] == '-') ? 1 : 0;

        // Check for hexadecimal numbers (e.g., "0x123")
        if (sz > start + 2 && chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false; // str is "0x"
            }
            for (; i < chars.length; i++) {
                if ((chars[i] < '0' || chars[i] > '9')
                        && (chars[i] < 'a' || chars[i] > 'f')
                        && (chars[i] < 'A' || chars[i] > 'F')) {
                    return false;
                }
            }
            return true; // str is a valid hex number
        }

        sz--; // Don't want to loop off the end of the array

        int i = start;

        while (i < sz || (i == sz && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false; // Not allowing signs after a digit has been seen
            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) { // Two decimal points or dec in exponent, return false.
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                if (hasExp) { // Two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true; // The number ends with an exponent, so signs are allowed.
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) { // Not allowing signs after a digit or exponent
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // Require another digit after an E
            } else {
                return false;
            }

            i++;
        }

        // Allow sign at the end of number (like "123.45E-2") only if we've seen digits or an exponent.
        return !allowSigns && foundDigit;
    }

    public NumberUtils() {
        super();
    }
}