void addNumber(double x) {
    char prev = getLastChar();
    
    // Correctly identifying and handling negative zero
    boolean isNegativeZero = x == 0.0 && Double.doubleToRawLongBits(x) < 0;
    
    // Handling the space to prevent misparsing issues such as `x--4`
    if (prev == '-' && (x < 0 || isNegativeZero)) {
        add(" ");
    }

    // Check if x has a decimal part or not, excluding special zero handling
    if (x == (long)x && !isNegativeZero) {
        add(Long.toString((long)x));
    } else {  // Default case for floating points
        add(Double.toString(x));
    }
}
