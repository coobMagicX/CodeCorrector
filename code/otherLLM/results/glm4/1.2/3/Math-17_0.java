public Dfp multiply(final int x) {
    return multiplyFast(x);
}

private Dfp multiplyFast(final int x) {
    Dfp result = newInstance(this);

    /* handle special cases */
    if (nans != FINITE) {
        if (isNaN()) {
            return this;
        }

        if (nans == INFINITE && x != 0) {
            result = newInstance(this);
            return result;
        }

        if (nans == INFINITE && x == 0) {
            field.setIEEEFlagsBits(DfpField.FLAG_INVALID);
            result = newInstance(getZero());
            result.nans = QNAN;
            result = dotrap(DfpField.FLAG_INVALID, MULTIPLY_TRAP, newInstance(getZero()), result);
            return result;
        }
    }

    /* range check x */
    if (x < 0 || x >= RADIX) {
        field.setIEEEFlagsBits(DfpField.FLAG_INVALID);
        result = newInstance(getZero());
        result.nans = QNAN;
        result = dotrap(DfpField.FLAG_INVALID, MULTIPLY_TRAP, result, result);
        return result;
    }

    int rh = 0;
    for (int i = 0; i < mant.length; i++) {
        final int r = mant[i] * x + rh;
        rh = r / RADIX;
        result.mant[i] = r - rh * RADIX;
    }

    int lostdigit = 0;
    if (rh != 0) {
        lostdigit = result.mant[0];
        result.shiftRight();
        result.mant[mant.length-1] = rh;
    }

    if (result.mant[mant.length-1] == 0) { // if result is zero, set exp to zero
        result.exp = 0;
    }

    final int excp = result.round(lostdigit);
    if (excp != 0) {
        result = dotrap(excp, MULTIPLY_TRAP, result, result);
    }

    return result;
}