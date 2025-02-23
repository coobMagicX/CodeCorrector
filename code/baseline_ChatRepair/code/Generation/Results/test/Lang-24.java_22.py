public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    str = str.trim(); // trim to eliminate leading/trailing spaces

    if (str.matches("-?0[xX][0-9a-fA-F]+")) { // Checks hexadecimal
        return true;
    }
    
    // Pattern matches numbers with optional negative sign, optional decimal and exponential parts.
    // Examples - "123", "-123", "123.45", "-123.45", "123e10", "-123E10", "123.45e-10"
    String decimalPattern = "-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?";
    return str.matches(decimalPattern);
}
