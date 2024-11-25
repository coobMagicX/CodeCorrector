public class Fraction {
    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    // Repairing the multiply method to handle large integers properly.
    public Fraction multiply(final int i) {
        if (i == 0) return new Fraction(0, 1);
        
        long resultNumerator = (long) numerator * i;
        long resultDenominator = (long) denominator;

        // Check for overflow
        if (resultNumerator / i != numerator || resultDenominator / denominator != denominator) {
            throw new ArithmeticException("Integer overflow occurred during multiplication.");
        }

        return new Fraction((int) resultNumerator, (int) resultDenominator);
    }

    public double percentageValue() {
        // Use the repaired multiply method to avoid integer overflow
        return multiply(100).doubleValue();
    }
}