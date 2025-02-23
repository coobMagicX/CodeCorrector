public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    str = str.trim();

    // Regular expression to handle most number cases including optional negative sign,
    // numbers with decimal points, and numbers in scientific notation.
    // This pattern does not handle hexadecimal numbers which are typically not considered
    // purely numeric by many standard libraries without explicit prefixes/suffixes in context.
    if (str.matches("-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?")) {
        return true;
    }

    // Check hexadecimal which is defined usually starting with "0x" or "0X".
    if (str.matches("-?0[xX][0-9a-fA-F]+")) {
        return true;
    }

    return false;
}
