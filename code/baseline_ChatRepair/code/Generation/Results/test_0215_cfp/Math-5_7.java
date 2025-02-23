public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    
    if (real == 0.0 && imaginary == 0.0) {
        return new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY); 
    }

    if (isInfinite) {
        return ZERO;
    }

    if (Math.abs(real) < Math.abs(imaginary)) {
        double q = real / imaginary;
        double denominator = real * q + imaginary;
        if (denominator == 0.0) {
            return new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        double scale = 1.0 / denominator;
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double denominator = imaginary * q +