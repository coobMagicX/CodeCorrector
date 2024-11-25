public static Long createLong(final String str) throws NumberFormatException {
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
        if(hexDigits <= 19){
            try{
                return Long.parseLong(str.substring(pfxLen));
            }catch(final NumberFormatException nfe){//NOPMD
                // ignore the bad number
            }
        }
        return createBigInteger(str);
    }

    //User doesn't have a preference on the return type, so let's start
    //small and go from there...
    if (str.charAt(0) == '-' && isDigits(str.substring(1))) {
        try {
            final Long l = Long.parseLong(str);
            if(l < 0){
                return createBigInteger(str);
            }
            return l;
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
    }

    //Must be a Float, Double, BigDecimal
    final boolean allZeros = isAllZeros(str);
    try {
        if(isDigits(str)){
            return Long.parseLong(str);
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    //Must be a Float, Double, BigDecimal
    final boolean allZeros = isAllZeros(str);
    try {
        if(isDigits(str)){
            return Long.parseLong(str);
        }
    } catch (final NumberFormatException nfe) { // NOPMD
        // ignore the bad number
    }

    return createBigInteger(str);
}