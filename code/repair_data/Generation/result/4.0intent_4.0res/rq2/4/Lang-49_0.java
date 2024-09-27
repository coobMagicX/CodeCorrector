public class Fraction {
    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Fraction getFraction(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    private int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    public Fraction reduce() {
        if (numerator == 0) {
            return new Fraction(0, 1);
        }

        int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
        int reducedNumerator = numerator / gcd;
        int reducedDenominator = denominator / gcd;

        // Correct the sign if the denominator is negative
        if (reducedDenominator < 0) {
            reducedNumerator = -reducedNumerator;
            reducedDenominator = -reducedDenominator;
        }

        return new Fraction(reducedNumerator, reducedDenominator);
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
}