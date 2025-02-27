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
    
    
    if (Math.abs(real) < Math.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1. / (real * q + imaginary);
        