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
            try {
                return createLong(str.substring(pfx.length()));
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            break;
        }
    }

    //User doesn't have a preference on the return type, so let's start
    //small and go from there...
    if (str.startsWith("-")) {
        try {
            return createInteger(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            return createLong(str);
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        return createBigInteger(str);
    }

    // Must be an Integer, Long, Biginteger
    try {
        return createInteger(str);
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }
    try {
        return createLong(str);
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }
    return createBigInteger(str);
}