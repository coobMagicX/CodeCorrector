public boolean equals(Object other) {
    boolean ret;
    
    if (this == other) { 
        ret = true;
    } else if (other == null || !(other instanceof Complex)) {
        ret = false;
    } else {
        Complex rhs = (Complex)other;
        // Check for NaN values
        if (Double.isNaN(this.real) || Double.isNaN(this.imaginary) || Double.isNaN(rhs.getReal()) || Double.isNaN(rhs.getImaginary())) {
            ret = Double.compare(Double.NaN, this.real) == 0 && Double.compare(Double.NaN, this.imaginary) == 0 &&
                   Double.compare(Double.NaN, rhs.getReal()) == 0 && Double.compare(Double.NaN, rhs.getImaginary()) == 0;
        } else {
            // No NaN values, perform the equality check
            ret = (this.real == rhs.getReal() || Double.compare(this.real, rhs.getReal()) == 0) &&
                   (this.imaginary == rhs.getImaginary() || Double.compare(this.imaginary, rhs.getImaginary()) == 0);
        }
    }
  
    return ret;
}