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
        int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
        if (gcd == 1) {
            return this;
        }
        return new Fraction(numerator / gcd, denominator / gcd);
    }

    private int greatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    public static void main(String[] args) {
        Fraction fraction = new Fraction(50, 100);
        Fraction reducedFraction = fraction.reduce();
        System.out.println("Reduced Fraction: " + reducedFraction.numerator + "/" + reducedFraction.denominator);
    }
}