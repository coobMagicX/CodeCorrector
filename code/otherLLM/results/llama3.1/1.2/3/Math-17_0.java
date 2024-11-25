public Dfp multiply(final int x) {
    if (isInfinity(this)) { // check for infinity
        if (x == 0 || isInfinity(new Dfp(x))) { // multiplication of infinity with anything results in NaN
            return getNaN();
        } else if (x != 0) { // multiplication of infinity with non-zero number results in infinite sign
            return getDfpInfiniteSign(this.isNegative());
        }
    }

    if (isZero(this)) { // multiplication by zero is always zero
        return new Dfp(0);
    }

    if (isNaN(this)) { // NaN multiplied by anything results in NaN
        return getNaN();
    }

    int result = multiplyFast(x); // call the fast multiply method

    // check for overflow/underflow and handle accordingly
    if ((isNegative(this) && result < 0) || (!isNegative(this) && result > 0)) {
        Dfp temp = new Dfp(result);
        return getDfpInfiniteSign(isNegative(temp));
    }

    return new Dfp(result);
}