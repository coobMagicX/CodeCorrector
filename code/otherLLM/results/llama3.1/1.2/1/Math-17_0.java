public Dfp multiply(final int x) {
    // Check for overflow handling, large numbers multiplication and edge case handling
    if (isInf(x)) { 
        return inf(); 
    } else if (isNan(x)) { 
        return nan(); 
    }

    // Multiply by zero only when the operand is zero
    if (x == 0) {
        return zero();
    }
    
    // Overflow handling and large numbers multiplication
    Dfp result = multiplyFast(x);
    if (result.isInf()) {
        return inf(); 
    } else if (result.isNan()) { 
        return nan();
    }

    return result;
}