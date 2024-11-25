public class NumberUtils {

    public static Number createNumber(String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }  
        if (str.startsWith("--")) {
            // this is protection for poorness in java.lang.BigDecimal.
            // it accepts this as a legal value, but it does not appear 
            // to be in specification of class. OS X Java parses it to 
            // a wrong value.
            return null;
        }
        if (str.startsWith("0x") || str.startsWith("-0x")) {
            return createInteger(str);
        }   
        char lastChar = str.charAt(str.length() - 1);
        boolean isLong = str.endsWith("L") || str.endsWith("l");
        boolean isDouble = str.endsWith("D") || str.endsWith("d");
        String numericStr = isLong || isDouble ? str.substring(0, str.length() - 1) : str;

        if (isLong && !isDigits(numericStr)) {
            return createBigInteger(str);
        }

        // User doesn't have a preference on the return type, so let's start
        // small and go from there...
        try {
            int value = Integer.parseInt(numericStr, 16);
            if (!isLong && !isDouble) {
                return value; // Return as integer
            }
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }

        // Fallback to BigInteger for very long numbers or double/long check
        try {
            if (isLong || isDouble) {
                BigInteger bigIntValue = new BigInteger(numericStr, 16);
                return createBigDecimal(bigIntValue); // Convert to BigDecimal if it's too large for int/long/float/double
            } else {
                return createInteger(str); // Otherwise, try integer again
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("The number '" + str + "' is not a valid long.");
        }
    }

    private static Integer createInteger(String numericStr) throws NumberFormatException {
        if (numericStr.startsWith("-")) {
            return Integer.parseInt(numericStr.substring(1), 16);
        } else {
            return Integer.parseInt(numericStr, 16);
        }
    }

    // isDigits and other helper methods are assumed to be defined elsewhere
}