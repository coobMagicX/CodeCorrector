public double percentageValue() {
    // Use long to handle large integer values that may cause overflow with int multiplication.
    long numeratorProduct = (long)numerator * 100;
    // Divide the result by the denominator. Since we are expecting an exact division,
    // casting to int is safe here as it will not cause overflow.
    int percentageValueInt = (int)(numeratorProduct / denominator);
    
    // Convert the integer result to a double and return it.
    return (double)percentageValueInt;
}