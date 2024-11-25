public final class Fraction {
    private int numerator;
    private int denominator;

    public static Fraction getReducedFraction(int numerator, int denominator) throws MathArithmeticException {
        if (denominator == 0) {
            throw new MathArithmeticException("Denominator cannot be zero");
        }
        // Implement reduction logic here
        return new Fraction(numerator, denominator);
    }

    private Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction add(Fraction other) throws MathArithmeticException {
        int sumDenom = multiply(denominator, other.denominator);
        int sumNum = (multiply(numerator, other.denominator)) + (multiply(other.numerator, denominator));
        return getReducedFraction(sumNum, sumDenom);
    }

    public Fraction subtract(Fraction other) throws MathArithmeticException {
        int subtrahendDenom = multiply(denominator, other.denominator);
        int subtrahendNum = (multiply(numerator, other.denominator)) - (multiply(other.numerator, denominator));
        return getReducedFraction(subtrahendNum, subtrahendDenom);
    }

    public Fraction multiply(Fraction other) throws MathArithmeticException {
        int prodNum = numerator * other.numerator;
        int prodDenom = denominator * other.denominator;
        return getReducedFraction(prodNum, prodDenom);
    }

    public Fraction divide(Fraction other) throws MathArithmeticException {
        if (other.numerator == 0) {
            throw new MathArithmeticException("Cannot divide by zero");
        }
        int resultNum = numerator * other.denominator;
        int resultDenom = denominator * other.numerator;
        return getReducedFraction(resultNum, resultDenom);
    }

    public double percentageValue() {
        // Implement logic to calculate the fraction as a percentage
        return 0.00; // Replace with actual calculation
    }
}