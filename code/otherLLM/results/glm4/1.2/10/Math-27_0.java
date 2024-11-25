public double percentageValue() {
    long numerator = this.getNumerator(); // Assuming getNumerator() returns an int, we convert to long to prevent overflow
    long denominator = this.getDenominator();

    // Check for integer overflow when multiplying by 100.
    if (numerator > Integer.MAX_VALUE / 100 || denominator > Integer.MAX_VALUE / 100) {
        throw new ArithmeticException("Integer overflow occurred during multiplication.");
    }

    // Perform the calculation using long to avoid overflow
    numerator *= 100;

    // Check for integer overflow after multiplication
    if (denominator == 0) {
        throw new ArithmeticException("Denominator cannot be zero.");
    } else if ((numerator / denominator) > Integer.MAX_VALUE) {
        throw new ArithmeticException("Resulting value is too large to fit in an int.");
    }

    // Cast the numerator to an integer before returning the double value
    return (double) numerator / denominator;
}