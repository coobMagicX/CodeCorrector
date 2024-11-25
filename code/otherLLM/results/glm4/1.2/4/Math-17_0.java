public Dfp multiply(final int x) {
    // Ensure that the Dfp instance is finite to perform multiplication with an integer
    if (this.nans != FINITE) {
        return this; // Return this if it's NaN or Infinite, as multiplication is not defined
    }

    // Initialize the result using newInstance to ensure a new object is created
    Dfp result = newInstance(this);

    /* handle special cases */
    if (this.nans == INFINITE && x != 0) {
        result = newInstance(this);
    } else if (this.nans == INFINITE && x == 0) {
        field.setIEEEFlagsBits(DfpField.FLAG_INVALID);
        result = newInstance(getZero());
        result.nans = QNAN;
        result = dotrap(DfpField.FLAG_INVALID, MULTIPLY_TRAP, newInstance(getZero()), result);
    } else if (x < 0 || x >= RADIX) {
        field.setIEEEFlagsBits(DfpField.FLAG_INVALID);
        result = newInstance(getZero());
        result.nans = QNAN;
        result = dotrap(DfpField.FLAG_INVALID, MULTIPLY_TRAP, result, result);
    } else {
        /* Perform the multiplication */
        int rh = 0; // Remainder for the addition
        for (int i = 0; i < this.mant.length; i++) {
            final int r = mant[i] * x + rh;
            rh = r / RADIX;
            result.mant[i] = r - rh * RADIX;
        }

        /* Handle the case where there is a remainder after processing all mantissa digits */
        if (rh != 0) {
            lostdigit = result.mant[0];
            result.shiftRight(); // Shift right to make room for the remainder
            result.mant[mant.length - 1] = rh;
        }

        /* Set exponent and adjust result */
        if (result.mant[result.mant.length - 1] == 0) { // If the result is zero, set exponent to zero
            result.exp = 0;
        } else {
            // Adjust exponent based on the amount of digits shifted right due to multiplication
            result.exp -= log10K(); // Subtracting log10K because it represents the shift in mantissa digits
        }

        /* Round the result */
        final int excp = result.round(lostdigit);
        if (excp != 0) {
            result = dotrap(excp, MULTIPLY_TRAP, result, result);
        }
    }

    return result;
}