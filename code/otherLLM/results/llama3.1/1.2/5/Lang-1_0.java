public static Number createNumber(String str) {
    // Remove any leading or trailing whitespace from the input string.
    str = str.trim();

    if (str.length() == 0) { // empty string
        return null; // or throw an exception, depending on requirements.
    }

    //Check for integer, long and biginteger first...
    try {
        // Try to create a Long object from the string. If successful, return it.
        final Long l = NumberUtils.createLong(str);
        if (l != null) { // null could be returned if the input was too large
            return l;
        }
    } catch (final NumberFormatException nfe) {
        // ignore and continue to next type
    }

    try {
        // Try to create a BigInteger object from the string. If successful, return it.
        final BigInteger bi = NumberUtils.createBigInteger(str);
        if (bi != null) { // null could be returned if the input was too large
            return bi;
        }
    } catch (final NumberFormatException nfe) {
        // ignore and continue to next type
    }

    // Now try for floats, doubles and bigdecimal...
    try {
        // Try to create a Float object from the string. If successful, return it.
        final Float f = NumberUtils.createFloat(str);
        if (f != null && !f.isInfinite()) { // ignore infinite values
            return f;
        }
    } catch (final NumberFormatException nfe) {
        // ignore and continue to next type
    }

    try {
        // Try to create a Double object from the string. If successful, return it.
        final Double d = NumberUtils.createDouble(str);
        if (d != null && !d.isInfinite()) { // ignore infinite values
            return d;
        }
    } catch (final NumberFormatException nfe) {
        // ignore and continue to next type
    }

    try {
        // Try to create a BigDecimal object from the string. If successful, return it.
        final BigDecimal bd = NumberUtils.createBigDecimal(str);
        if (bd != null) { // this one cannot be null, so don't check for it
            return bd;
        }
    } catch (final NumberFormatException nfe) {
        // ignore and continue to next type
    }

    // If none of the above works, throw an exception.
    throw new NumberFormatException("Could not create a number from the input string: " + str);
}