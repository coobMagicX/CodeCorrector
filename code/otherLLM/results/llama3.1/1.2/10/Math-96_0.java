public boolean equals(Object other) {
    boolean ret;

    if (this == other) { 
        ret = true;
    } else if (other == null) {
        ret = false;
    } else  {
        try {
            Complex rhs = (Complex)other;
            double realDiff = Math.abs(real - rhs.getReal());
            double imaginaryDiff = Math.abs(imaginary - rhs.getImaginary());
            
            // consider both the absolute difference and NaN values
            if ((realDiff < 1e-12 && imaginaryDiff < 1e-12) || (rhs.isNaN() && this.isNaN())) {
                ret = true;
            } else {
                ret = false;
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }

    return ret;
}