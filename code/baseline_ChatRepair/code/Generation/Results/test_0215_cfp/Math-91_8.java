import java.math.BigDecimal;

public int compareTo(Fraction object) {
    
    BigDecimal nOd = BigDecimal.valueOf(numerator).divide(BigDecimal.valueOf(denominator), MathContext.DECIMAL128);
    BigDecimal