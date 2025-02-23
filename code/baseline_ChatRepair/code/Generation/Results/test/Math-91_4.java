import java.math.BigDecimal;

public int compareTo(Fraction object) {
    
    BigDecimal lhs = BigDecimal.valueOf(this.numerator).multiply(BigDecimal.valueOf(object.denominator));
    BigDecimal rhs = BigDecimal.valueOf(object.numerator).multiply(BigDecimal.valueOf(this.denominator