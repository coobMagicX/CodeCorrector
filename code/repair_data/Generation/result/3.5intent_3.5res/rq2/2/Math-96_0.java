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
                ret = multiply(createComplex(real, -imaginary)).equals(createComplex(rhs.getReal(), -rhs.getImaginary()))
                        && multiply(createComplex(imaginary, real)).equals(createComplex(rhs.getImaginary(), rhs.getReal())); 
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
  
    return ret;
}