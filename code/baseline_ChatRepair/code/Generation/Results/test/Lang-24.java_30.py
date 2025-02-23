public static boolean isNumber(String str) {
    if (str == null) {
        return false;
    }
    str = str.trim();
    if (str.length() == 0) {
        return false;
    }
    // Regular expression to match valid numbers.
    // This pattern handles optional signs, decimals, scientific notation (e.g., 1e-30), hexadecimals, and type qualifiers (f, d, l).
    return str.matches("[+-]?((\\d+\\.?\\d*)|(\\.\\d+))([eE][+-]?\\d+)?[fFdD]?") || // Decimal or scientific notation
           str.matches("0[xX][0-9a-fA-F]+") || // Hexadecimal
           str.matches("[+-]?\\d+[lL]?"); // Integer with optional L for long types
}
