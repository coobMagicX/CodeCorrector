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
                double epsilon = 1e-9; // define a small tolerance for floating-point comparison
                ret = Math.abs(real - rhs.getReal()) <= epsilon && Math.abs(imaginary - rhs.getImaginary()) <= epsilon;
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
  
    return ret;
}