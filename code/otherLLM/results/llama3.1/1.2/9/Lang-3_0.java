public static Number createNumber(final String str) throws NumberFormatException {
    // ...

    // Improve decimal handling
    if (decPos > -1) { 
        mant = str.substring(0, decPos); 
        numDecimals = dec.length(); 
    } else {
        if (expPos > -1) {
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
        numDecimals = 0; // Reset to zero decimals
    }

    // ...

    final boolean allZeros = isAllZeros(mant) && (dec == null ? true : isAllZeros(dec));

    // Check for overflow/underflow when converting strings to integers, longs, floats, or doubles
    try {
        return createInteger(str);
    } catch (final NumberFormatException nfe) { 
        if (!str.startsWith("0x") && !str.startsWith("-0x")) { 
            // Ignore hex conversion failures
            try {
                final int i = Integer.parseInt(str);
                if (i > Integer.MAX_VALUE || i < Integer.MIN_VALUE) { 
                    // Check for integer overflow/underflow
                    return createLong(str);
                }
            } catch (final NumberFormatException e) { 
                // Ignore parsing failures
            }
        }
    }

    try {
        final Float f = createFloat(str);
        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
            return f;
        }
    } catch (final NumberFormatException nfe) { 
        // Ignore parsing failures
    }

    try {
        final Double d = createDouble(str);
        if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
            return d;
        }
    } catch (final NumberFormatException nfe) { 
        // Ignore parsing failures
    }

    // ...
}