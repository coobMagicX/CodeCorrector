public Dfp multiply(final int x) {
    if (x == 0) { // overflow/underflow handling for multiplying by zero
        return this;
    } else if (Double.isInfinite(x) || Double.isNaN(x)) { // infinity and NaN handling
        return new Dfp(Double.POSITIVE_INFINITY); // assume positive infinity for simplicity
    } else {
        return multiplyFast(x);
    }
}