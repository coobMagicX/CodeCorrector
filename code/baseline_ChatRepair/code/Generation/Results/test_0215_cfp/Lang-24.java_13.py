public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    
    // Check for hex representation
    if (str.startsWith("-0x") || str.startsWith("0x") || str.startsWith("+0x")) {
        try {
            Long.parseLong(str.replaceFirst("[-+]0x", ""), 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check for standard decimal, integer and scientific notation representation
    try {
        Double.parseDouble(str);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
