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
                double realCompare = Math.abs(this.real - rhs.getReal());
                double imaginaryCompare = Math.abs(this.imaginary - rhs.getImaginary());
                ret = (realCompare < 1e-9) && (imaginaryCompare < 1e-9);
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
  
    return ret;
}