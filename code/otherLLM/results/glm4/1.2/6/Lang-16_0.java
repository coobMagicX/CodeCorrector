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
        // Convert the string to uppercase for consistent parsing
        String hexStr = str.toUpperCase();
        try {
            long value = Long.parseLong(hexStr, 16);
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                throw new NumberFormatException("Value out of range for int type");
            }
            return value;
        } catch (NumberFormatException e) {
            // Handle exception or return default value
        }
    } else if ((str.startsWith("-") && str.length() == 2 && Character.isDigit(str.charAt(1)))) {
        // Handle negative zero as a special case, which is not a valid integer representation
        throw new NumberFormatException("Not a valid integer: " + str);
    }

    // Additional parsing logic for other number types can be added here...
}