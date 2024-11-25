public class NumberUtils {
    public static Integer createInteger(String str) throws NumberFormatException {
        if (str.startsWith("0x")) {
            return Long.parseLong(str.substring(2), 16);
        } else {
            return Integer.parseInt(str);
        }
    }

    public static Long createLong(String str) throws NumberFormatException {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            // Try BigInteger
            return new BigInteger(str).longValue();
        }
    }

    public static Float createFloat(String str) throws NumberFormatException {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            if (str.contains("E")) {
                // Handle scientific notation
                String[] parts = str.split("E");
                int exponent = Integer.parseInt(parts[1]);
                float mantissa = Float.parseFloat(parts[0]);
                if (exponent < -126 || exponent > 127) {
                    throw new NumberFormatException("Invalid exponent: " + exponent);
                }
                return (float) Math.pow(10, exponent) * mantissa;
            } else {
                // Handle non-scientific notation infinity
                if (str.equals("Infinity")) {
                    return Float.POSITIVE_INFINITY;
                } else if (str.equals("-Infinity")) {
                    return Float.NEGATIVE_INFINITY;
                }
            }
        }
    }

    public static Double createDouble(String str) throws NumberFormatException {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            // Try float
            return createFloat(str).doubleValue();
        }
    }

    public static BigInteger createBigInteger(String str) {
        try {
            return new BigInteger(str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Cannot parse string: " + str);
        }
    }
}