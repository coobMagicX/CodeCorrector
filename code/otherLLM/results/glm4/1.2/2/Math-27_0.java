public double percentageValue() {
    long result; // Use a larger numeric type to handle potential overflow
    if (numerator >= 0) {
        result = (long) numerator * 100;
    } else {
        result = -((long) numerator * 100);
    }
    if (denominator != 0 && numerator != 0) { // Check for division by zero
        return result / denominator; // Perform the division to get the percentage value
    }
    return 0; // Return 0 in case of division by zero or invalid state
}