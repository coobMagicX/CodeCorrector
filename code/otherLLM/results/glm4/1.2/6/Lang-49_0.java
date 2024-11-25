public class Fraction {
    private final int numerator;
    private final int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        this.numerator = numerator;
        this.denominator = Math.abs(denominator);
    }

    public static int greatestCommonDivisor(int a, int b) {
        // Assuming the method 'greatestCommonDivisor' exists with correct implementation
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    public static Fraction getFraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        // Assuming this method creates and returns a new Fraction object
        return new Fraction(numerator, denominator);
    }

    public Fraction reduce() {
        if (denominator < 0) {
            numerator = -numerator; // Make sure numerator is positive before finding GCD
        }
        int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
        
        if (gcd == 1) {
            return this;
        } else {
            return Fraction.getFraction(numerator / gcd, denominator / gcd);
        }
    }

    // Getters and setters for numerator and denominator (if necessary)
    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }
}

// Example usage:
// Fraction fraction = new Fraction(12, 18);
// Fraction reducedFraction = fraction.reduce();