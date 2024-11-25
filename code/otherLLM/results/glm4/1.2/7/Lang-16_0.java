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
    if (str.startsWith("0x") || str.startsWith("0X")) {
        // Handle hexadecimal numbers
        return createBigInteger(str.substring(2));
    } else if (str.startsWith("0b") || str.startsWith("0B")) {
        // Handle binary numbers
        return createBigInteger(Long.toString(Long.parseLong(str.substring(2), 2)));
    } else if (str.startsWith("0")) {
        // Handle octal numbers
        return createBigInteger(Long.toString(Long.parseLong(str.substring(1), 8)));
    } else if (str.startsWith("0.")) {
        // Handle floating-point numbers with leading zero
        return createBigDecimal(str);
    } else {
        // User does not have a preference on the return type, so let's start
        // small and go from there...
        try {
            Integer i = Integer.parseInt(str);
            return i;
        } catch (NumberFormatException e) {}
        
        try {
            Long l = Long.parseLong(str);
            return l;
        } catch (NumberFormatException e) {}
        
        return createBigDecimal(str);
    }
}

// Helper method to handle hexadecimal and binary numbers
private static BigInteger parseAsBigInteger(String numeric) throws NumberFormatException {
    if (numeric.startsWith("0x") || numeric.startsWith("0X")) {
        return new BigInteger(numeric.substring(2), 16);
    } else if (numeric.startsWith("0b") || numeric.startsWith("0B")) {
        return new BigInteger(numeric.substring(2), 2);
    } else if (numeric.startsWith("0")) {
        return new BigInteger(numeric.substring(1), 8);
    }
    // Assume decimal if no other prefix
    return new BigInteger(numeric);
}