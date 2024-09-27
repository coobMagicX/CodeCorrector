public static Number createNumber(final String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }
    // Need to deal with all possible hex prefixes here
    final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};
    int pfxLen = 0;
    for(final String pfx : hex_prefixes) {
        if (str.startsWith(pfx)) {
            pfxLen += pfx.length();
            break;
        }
    }
    if (pfxLen > 0) { // we have a hex number
        final int hexDigits = str.length() - pfxLen;
        if (hexDigits > 16) { // too many for Long
            return createBigInteger(str);
        }
        if (hexDigits > 8) { // too many for an int
            return createLong(str);
        }
        // Check if the number is negative
        boolean isNegative = false;
        if (str.startsWith("-")) {
            isNegative = true;
        }
        // Remove the prefix and convert to decimal
        String decimalStr = str.substring(pfxLen);
        BigDecimal decimal = new BigDecimal(decimalStr);
        // Convert to the appropriate Number type based on the decimal value
        if (decimal.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) <= 0 && decimal.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) >= 0) {
            if (isNegative) {
                return Integer.parseInt(decimalStr, 16) * -1;
            } else {
                return Integer.parseInt(decimalStr, 16);
            }
        } else if (decimal.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) <= 0 && decimal.compareTo(BigDecimal.valueOf(Long.MIN_VALUE)) >= 0) {
            if (isNegative) {
                return Long.parseLong(decimalStr, 16) * -1;
            } else {
                return Long.parseLong(decimalStr, 16);
            }
        } else {
            if (isNegative) {
                return new BigInteger(decimalStr, 16).negate();
            } else {
                return new BigInteger(decimalStr, 16);
            }
        }
    }
    // Rest of the code remains the same
}