public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }
    // Trim the string to handle spaces.
    str = str.trim();
    
    try {
        // This handles most decimal and signed numbers including those in scientific notation
        Double.parseDouble(str);
        return true;
    } catch (NumberFormatException e) {
        // If Double.parseDouble fails, it could still be a hex number.
        if (str.startsWith("0x") || str.startsWith("0X") || str.startsWith("-0x") || str.startsWith("-0X")) {
            try {
                // We can parse it as a BigInteger. There's no risk of numbers being too large.
                new java.math.BigInteger(str.replaceFirst("0x", "").replaceFirst("0X", "").replaceFirst("-0x", "-").replaceFirst("-0X", "-"), 16);
                return true;
            } catch(NumberFormatException nfe) {
                // If it fails, it's not a valid hex number.
                return false;
            }
        } else {
            // Not a valid format or ordinary string that can't be a number.
            return false;
        }
    }
}
