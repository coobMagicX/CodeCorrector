public Dfp multiply(final int x) {
    if (x == 0) { // Handle multiplication by zero
        return Dfp.ZERO;
    } else if (Double.isInfinite((double)x)) { // Handle positive or negative infinity
        return x > 0 ? this : this.negate();
    }

    long absX = Math.abs(x);
    long absY = getMagnitude();

    if (absX < Dfp.MANTISSA_BITS && absY < Dfp.MANTISSA_BITS) {
        // Basic multiplication for smaller numbers
        return multiplyFast(x);
    } else {
        // Handle large numbers by using the fast multiplication method
        // with the absolute values and then applying the sign
        long result = multiplyFast(absX, absY);

        if (x < 0) {
            return result.negate();
        }

        return new Dfp(result, getExponent() + Math.max(0, Math.min(Dfp.MANTISSA_BITS - countLeadingZeros(absX), Dfp.MANTISSA_BITS - countLeadingZeros(absY))));
    }
}