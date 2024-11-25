public double percentageValue() {
    // Implement proper overflow handling by checking for fraction's denominator being zero
    if (denominator == 0) {
        throw new ArithmeticException("Cannot calculate percentage value for a fraction with zero denominator");
    }
    
    return multiply(100).doubleValue();
}