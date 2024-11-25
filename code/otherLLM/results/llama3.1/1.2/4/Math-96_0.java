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
                // Check for conjugate pairs
                double rhsReal = rhs.getReal();
                double rhsImaginary = rhs.getImaginary();
                double conjRhsReal = -rhsReal;
                double conjRhsImaginary = rhsImaginary;
                
                ret = (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhsReal)) && 
                      (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhsImaginary)) || 
                      (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(conjRhsReal)) && 
                      (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(conjRhsImaginary));
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
  
    return ret;
}