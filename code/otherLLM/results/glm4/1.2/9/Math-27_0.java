public double percentageValue() {
    // First, we'll convert our Fraction to a double value before multiplying by 100.
    // This is to avoid potential overflow issues with large numerators/denominators.
    double fractionValue = numerator / (double)denominator;
    return multiply(100).doubleValue();
}