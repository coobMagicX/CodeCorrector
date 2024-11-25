public class NumberUtils {

    private static final int MAX_DIGITS_FOR_FLOAT = 7;
    private static final int MAX_DIGITS_FOR_DOUBLE = 16;

    public static Number createNumber(String numeric) {
        if (numeric == null || numeric.isEmpty()) {
            throw new IllegalArgumentException("Numeric string is empty or null");
        }

        // Check for trailing 'l' or 'L' for long, 'f' or 'F' for float/double, 'd' or 'D' for double
        String numericWithoutSuffix = numeric;
        if (numeric.endsWith("l") || numeric.endsWith("L")) {
            numericWithoutSuffix = numeric.substring(0, numeric.length() - 1);
        } else if (numeric.endsWith("f") || numeric.endsWith("F")) {
            numericWithoutSuffix = numeric.substring(0, numeric.length() - 1);
        } else if (numeric.endsWith("d") || numeric.endsWith("D")) {
            numericWithoutSuffix = numeric.substring(0, numeric.length() - 1);
        }

        try {
            // Try to parse as long
            return createLong(numericWithoutSuffix);
        } catch (NumberFormatException e) {
            try {
                // Try to parse as float or double if the string has a suffix 'f' or 'd'
                if ((numeric.endsWith("f") || numeric.endsWith("F")) && isDigits(numericWithoutSuffix)) {
                    return createFloat(numericWithoutSuffix);
                } else if (numeric.endsWith("d") || numeric.endsWith("D")) {
                    return createDouble(numericWithoutSuffix);
                }
            } catch (NumberFormatException ex) {
                // If it's neither a long, float, nor double, try as BigDecimal
                return createBigDecimal(numericWithoutSuffix);
            }
        }

        // Fallback, should not reach here if the input is valid
        throw new NumberFormatException("Cannot parse number: " + numeric);
    }

    private static boolean isDigits(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Long createLong(String numeric) throws NumberFormatException {
        long value;
        try {
            value = Long.parseLong(numeric);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cannot parse number as long: " + numeric);
        }
        return value;
    }

    private static Float createFloat(String numeric) throws NumberFormatException {
        float value;
        try {
            value = Float.parseFloat(numeric);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cannot parse number as float: " + numeric);
        }
        return value;
    }

    private static Double createDouble(String numeric) throws NumberFormatException {
        double value;
        try {
            value = Double.parseDouble(numeric);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cannot parse number as double: " + numeric);
        }
        return value;
    }

    private static BigDecimal createBigDecimal(String numeric) throws NumberFormatException {
        BigDecimal value;
        try {
            value = new BigDecimal(numeric);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cannot parse number as BigDecimal: " + numeric);
        }
        return value;
    }

    // Dummy methods to simulate the usage of utility methods within the context
    private static boolean isAllZeros(String str) {
        return "0".equals(str.trim());
    }

    public static void main(String[] args) {
        System.out.println("NumberUtils.createNumber('1234567890123456') = " + createNumber("1234567890123456"));
        System.out.println("NumberUtils.createNumber('123.456789f') = " + createNumber("123.456789f"));
        System.out.println("NumberUtils.createNumber('-1234567890123456L') = " + createNumber("-1234567890123456L"));
    }
}