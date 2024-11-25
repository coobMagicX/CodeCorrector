public double percentageValue() {
    // Multiply numerator by 100 and divide by denominator to get percentage value
    double multiplier = (double) (denominator / numerator) * 100;
    
    // Handle overflow in integer division and ensure accurate double representation of fractional values
    if (multiplier == (int) multiplier) {
        return (int) multiply(multiplier).doubleValue();
    } else {
        return multiply(100).doubleValue();
    }
}