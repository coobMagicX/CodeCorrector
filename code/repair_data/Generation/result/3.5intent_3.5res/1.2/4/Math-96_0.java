public boolean equals(Object other) {
    boolean ret;
    
    if (this == other) { 
        ret = true;
    } else if (other == null) {
        ret = false;
    } else  {
        try {
            Complex rhs = (Complex)other;
            if (rhs.isNaN()) {
                ret = this.isNaN();
            } else {
                ret = (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal())) && (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary())); 
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
  
    return ret;
}

public Complex multiply(Complex rhs) {
    if (isNaN() || rhs.isNaN()) {
        return NaN;
    }
    if (Double.isInfinite(real) || Double.isInfinite(imaginary) ||
        Double.isInfinite(rhs.real)|| Double.isInfinite(rhs.imaginary)) {
        // we don't use Complex.isInfinite() to avoid testing for NaN again
        return INF;
    }
    
    // Fix the multiply method to compute the multiplication of two Complex numbers
    double newReal = (real * rhs.real) - (imaginary * rhs.imaginary);
    double newImaginary = (real * rhs.imaginary) + (imaginary * rhs.real);
    
    return createComplex(newReal, newImaginary);
}