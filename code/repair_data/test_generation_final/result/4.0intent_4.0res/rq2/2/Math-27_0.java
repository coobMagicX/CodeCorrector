public double percentageValue() {
    return multiply(100).doubleValue();
}

public Fraction multiply(final int i) {
    return new Fraction(numerator * i, denominator);
}

// Assuming the existence of the following method in the class
public double doubleValue() {
    return (double) numerator / denominator;
}