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

    public Fraction reduce() {
        if (numerator == 0) {
            return new Fraction(0, 1);
        }
        
        // Ensure the gcd calculation uses the absolute values and denominator is positive
        int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
        int sign = (denominator < 0) ? -1 : 1; // Adjust the sign if the denominator is negative

        // Adjust both numerator and denominator by the calculated gcd and account for sign
        return new Fraction(sign * numerator / gcd, Math.abs(denominator) / gcd);
    }

    private int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    // Factory method to create a Fraction
    public static Fraction getFraction(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    // Getter methods for numerator and denominator
    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
}