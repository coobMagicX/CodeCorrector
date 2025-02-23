public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    
    if (real == 0.0 && imaginary == 0.0) {
        return createComplex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    if (isInfinite) {
        return ZERO;
    }

    double squaredSum = real * real + imaginary * imaginary;  

    if (squaredSum == 0.0) {
        
        
        
        return createComplex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    } else {
        