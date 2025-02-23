public Complex reciprocal() {
    if (isNaN) {
        return NaN;  
    }

    if (real == 0.0 && imaginary == 0.0) {
        return INF;  
    }

    if (isInfinite) {
        return ZERO;  
    }

    double scale = real * real + imaginary * imaginary;  

    
    
    if (scale == 0) {
        return INF;  
    }

    