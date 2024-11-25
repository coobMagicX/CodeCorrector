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
                Complex result1 = multiply(rhs);
                Complex result2 = rhs.multiply(this);
                
                // Consider two complex numbers mathematically equivalent if their 
                // product and the reverse operation on them are equal.
                ret = (result1.real == result2.getReal()) && (result1.imaginary == result2.getImaginary());
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
  
    return ret;
}