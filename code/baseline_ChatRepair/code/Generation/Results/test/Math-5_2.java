public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        
        return Complex.INFINITY;
    }

    if (isInfinite) {
        
        return Complex.ZERO;
    }

    if (Math.abs(real) < Math.abs(imaginary)) {
        
        double q = real / imaginary;
        double scale = 1.0 / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        
        double q =