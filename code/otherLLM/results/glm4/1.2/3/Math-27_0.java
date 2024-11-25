public class Fraction {

    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    private double multiply(Fraction fraction) {
        long numProduct = (long) numerator * fraction.getNumerator();
        long denProduct = (long) denominator * fraction.getDenominator();
        
        // Check for overflow
        if ((numProduct > Integer.MAX_VALUE || numProduct < Integer.MIN_VALUE) ||
            (denProduct == 0)) {
            throw new ArithmeticException("Overflow occurred in multiplication");
        }
        
        return (double) numProduct / denProduct;
    }

    public double percentageValue() {
        Fraction hundred = new Fraction(100, 1);
        double result = multiply(new Fraction(numerator, denominator)).doubleValue();
        double scaledResult = multiply(hundred).doubleValue();

        // Using Math.ulp for precision check
        double ulp = Math.ulp(scaledResult);

        return (scaledResult * (100 / scaledResult)) + ulp;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }
}