import java.math.BigDecimal;

public class Fraction {

    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) throw new IllegalArgumentException("Denominator cannot be zero.");
        this.numerator = numerator;
        this.denominator = denominator;
    }

    private static BigDecimal multiply(BigDecimal value, int multiplier) {
        return value.multiply(new BigDecimal(multiplier));
    }

    public double percentageValue() {
        BigDecimal numeratorBD = new BigDecimal(this.numerator);
        BigDecimal denominatorBD = new BigDecimal(this.denominator);
        
        // Calculate the result as a BigDecimal to prevent overflow
        BigDecimal result = multiply(numeratorBD, 100)
                             .divide(denominatorBD, BigDecimal.ROUND_HALF_UP);

        return result.doubleValue();
    }

    // Assume other methods like divide and abs are implemented properly
    // ...
}