public double percentageValue() {
    return multiply(100).doubleValue();
}

public Fraction multiply(final int i) {
    return new Fraction(numerator * i, denominator);
}