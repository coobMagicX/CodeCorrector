public static Number createNumber(String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }  
    if (str.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
        // it accepts this as a legal value, but it does not appear 
        // to be in specification of class. OS X Java parses it to 
        // a wrong value.
        return null;
    }
    if (str.startsWith("0x") || str.startsWith("-0x")) {
        // Check for valid hexadecimal characters
        String hexString = str.substring(2); // Skip the "0x" or "-0x" part
        if (!hexString.matches("[0-9a-fA-F]+")) {
            throw new IllegalArgumentException("Invalid character in hexadecimal string: " + str);
        }
        return createInteger(hexString);
    }   
    char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    // ... rest of the method remains unchanged ...
}