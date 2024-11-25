public Dfp multiply(final int x) {
    if (x == 0) { // Special case multiplication by zero
        return field.newDfp("0");
    }
    
    if (!field.isValidNumber(x)) { // Validate input
        throw new IllegalArgumentException("Invalid input: " + x);
    }

    long m = Math.abs(field.getLongValue(this));
    long n = Math.abs(field.getLongValue(field.newDfp(x)));

    // Handle overflow/underflow
    if ((m > field.getMaxValue() / 2) || (n > field.getMaxValue() / 2)) {
        return multiplySlow(x);
    }

    if (field.isZero(m) && !field.isZero(n)) { // Multiply by zero (zero factor)
        return new Dfp(0, field.getScale());
    }
    
    // Infinity and NaN handling
    if ((m == Long.MAX_VALUE && x < 0) || (n == Long.MIN_VALUE && x > 0)) {
        return field.newDfp(field.INF_NEG);
    } else if ((m == Long.MAX_VALUE && x > 0) || (n == Long.MIN_VALUE && x < 0)) {
        return field.newDfp(field.INF_POS);
    } else if (!field.isValidNumber(m * n)) { // Special case for NaN
        return field.newDfp(field.NAN);
    }

    long result = m * n;
    
    // Large integer multiplication
    if (result >= 0 && result < Math.abs(field.getMinValue())) {
        return new Dfp(result, field.getScale());
    } else {
        return multiplySlow(x); // Delegate to slow method for precise result
    }
}