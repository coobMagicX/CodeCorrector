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
                double lhsReal = real, lhsImaginary = imaginary;
                double rhsReal = rhs.getReal(), rhsImaginary = rhs.getImaginary();
                
                // Consider handling cases where the input object is not an instance of Complex
                if (rhs instanceof Number && !(rhs instanceof Complex)) {
                    Number num = (Number) rhs;
                    rhsReal = num.doubleValue();
                    rhsImaginary = 0.0;
                }
                
                ret = (Double.doubleToRawLongBits(lhsReal) == Double.doubleToRawLongBits(rhsReal)) 
                        && (Double.doubleToRawLongBits(lhsImaginary) == Double.doubleToRawLongBits(rhsImaginary));
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
  
    return ret;
}