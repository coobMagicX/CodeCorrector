public Dfp multiply(final int x) {
    if (x == 0) {
        return Dfp.ZERO; // Assuming Dfp.ZERO is a valid static reference for a zero value in Dfp class
    }
    return multiplyFast(x);
}