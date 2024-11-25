import java.math.BigInteger;

public static Number createNumber(String str) throws NumberFormatException {
    if (str == null || str.trim().isEmpty()) {
        throw new NumberFormatException("A blank or null string is not a valid number");
    }

    // Remove trailing zeros for floating-point numbers and trailing 'e'/'E' with sign
    str = str.replace(".", "").replace("0", "");
    str = str.replaceAll("[+|-]?[e|E][+|-]?$", "");

    if (str.matches(".*[+-]{2,}.*")) {
        throw new NumberFormatException("Multiple signs are not allowed: " + str);
    }
    if ((str.endsWith("e") || str.endsWith("E")) && !str.endsWith("e+") && !str.endsWith("e-") && !str.endsWith("E+")
            && !str.endsWith("E-")) {
        throw new NumberFormatException("Extra 'e' or 'E' with no sign is not allowed: " + str);
    }

    boolean allZeros = str.isEmpty() || str.equals("0");

    if (allZeros) {
        return BigInteger.ZERO;
    }

    // Check for long
    if (str.matches("-?\\d+")) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            // Ignore, will try the next type
        }
    }

    // Check for float or double
    if (str.matches("-?\\d+(\\.\\d+)?([e|E][+-]?\\d+)?")) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException ex) {
                // Ignore, will try the next type
            }
        }
    }

    // If none of the above types fit, then it must be a BigInteger
    return new BigInteger(str);
}