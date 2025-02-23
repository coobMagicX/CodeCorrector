public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    String number = str.trim();

    // Check for hex numbers starting with the "0x" prefix.
    if (number.startsWith("0x") || number.startsWith("-0x") || number.startsWith("+0x")) {
        try {
            // This will throw NumberFormatException if it's not a valid hex.
            Long.parseLong(number.replaceFirst("0x", ""), 16);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    // Try parsing as a decimal number
    try {
        Double.parseDouble(number);
        return true;
    } catch (NumberFormatException nfe) {
        return false;
    }
}
