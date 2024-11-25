public Dfp multiply(final int x) {
    if (x <= 0) {
        throw new IllegalArgumentException("The multiplier must be a positive integer.");
    }
    
    long longValue = x;
    Dfp result = multiplyFast(longValue);

    // Ensure the rounding mode is set to round up when the high part of the result exceeds the max value
    if (result.high() > Integer.MAX_VALUE) {
        return new Dfp(result.getHighPart() / Integer.MAX_VALUE, result.getLowPart(), result.isNegative());
    }
    
    return result;
}