public double percentageValue() {
    return multiply(100).toDouble();
}

public double toDouble() {
    return (double)numerator / denominator;
}

public Fraction multiply(final int i) {
    return new Fraction(numerator * i, denominator);
}