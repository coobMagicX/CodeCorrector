public Dfp multiply(final int x) {
    // Perform overflow check before calling multiplyFast
    if (x < 0 || x >= RADIX) {
        field.setIEEEFlagsBits(DfpField.FLAG_INVALID);
        return newInstance(getZero());
    }
    
    return multiplyFast(x);
}