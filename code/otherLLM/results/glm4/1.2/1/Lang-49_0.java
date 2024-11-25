public class Fraction {
    private int numerator;
    private int denominator;

    // Constructor to initialize fraction values
    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    // Method to simplify the fraction by reducing it using its GCD
    public Fraction reduce() {
        int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
        if (gcd == 1) {
            return this; // Return the original fraction object if no simplification is possible
        }
        return new Fraction(numerator / gcd, denominator / gcd);
    }

    // Method to calculate the Greatest Common Divisor of two numbers
    private static int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return Math.abs(a); // Ensure the GCD is non-negative
    }

    // Method to get the fraction as string
    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    // Getters for numerator and denominator
    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }
}

// Example of usage:
public class Main {
    public static void main(String[] args) {
        Fraction fraction = new Fraction(12, 18);
        System.out.println("Original: " + fraction); // Output: Original: 12/18
        Fraction reducedFraction = fraction.reduce();
        System.out.println("Reduced: " + reducedFraction); // Output: Reduced: 2/3
    }
}