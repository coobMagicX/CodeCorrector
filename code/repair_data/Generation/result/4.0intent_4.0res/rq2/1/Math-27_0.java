public double percentageValue() {
    return multiply(100).doubleValue();
}

public Fraction multiply(final int i) {
    return new Fraction(numerator * i, denominator);
}

public double doubleValue() {
    return (double) numerator / denominator;
}