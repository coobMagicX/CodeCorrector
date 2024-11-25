public class NumberUtils {

    public static boolean isNumber(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        // Extract methods for better readability
        if (!isValidSign(str, 0)) {
            return false; // Sign character not found at the start
        }
        int numType = identifyNumberType(str);
        switch (numType) {
            case INTEGER:
                return validateIntegerFormat(str);
            case FLOATING_POINT:
                return validateFloatingPointFormat(str);
            case EXPONENTIAL_NOTATION:
                return validateExponentialNotationFormat(str);
            case HEXADECIMAL:
                return validateHexadecimalFormat(str);
            default:
                return false; // Unknown number type
        }
    }

    private static boolean isValidSign(String str, int index) {
        char sign = str.charAt(index);
        return (sign == '-' || sign == '+');
    }

    private static int identifyNumberType(String str) {
        // Check for integer or floating-point formats
        if (str.contains(".")) {
            // Floating-point format detected
            return FLOATING_POINT;
        } else if (str.startsWith("0x") || str.startsWith("0X")) {
            // Hexadecimal format detected
            return HEXADECIMAL;
        } else {
            // Integer or exponential notation formats detected
            int exponentIndex = str.indexOf('E');
            if (exponentIndex != -1) {
                return EXPONENTIAL_NOTATION;
            }
            return INTEGER;
        }
    }

    private static boolean validateIntegerFormat(String str) {
        // Implement validation logic for integer format
    }

    private static boolean validateFloatingPointFormat(String str) {
        // Implement validation logic for floating-point format
    }

    private static boolean validateExponentialNotationFormat(String str) {
        // Implement validation logic for exponential notation format
    }

    private static boolean validateHexadecimalFormat(String str) {
        // Implement validation logic for hexadecimal format
    }
}