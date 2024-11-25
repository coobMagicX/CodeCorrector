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

    // Existing method that you can use
    private static int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    public Fraction reduce() {
        // Ensure the numerator and denominator are not both zero
        if (this.numerator == 0 && this.denominator == 0) {
            throw new ArithmeticException("Cannot reduce fraction to lowest terms with both numerator and denominator being zero.");
        }

        int gcd = greatestCommonDivisor(Math.abs(this.numerator), Math.abs(this.denominator));
        // Return a new instance of Fraction to avoid returning 'this'
        return new Fraction(this.numerator / gcd, this.denominator / gcd);
    }

    // Other methods (getters, setters, toString, etc.) can be added here
}