public static boolean isHex(String str) {
    return str.matches("0[xX][0-9a-fA-F]+");
}

public static boolean isOct(String str) {
    return str.matches("0[oO]?[0-7]+");
}