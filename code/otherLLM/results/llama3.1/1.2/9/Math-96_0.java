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
                // Calculate the product of both numbers and compare it to NaN
                Complex product = multiply(rhs);
                ret = product.isNaN() && this.isNaN(); 
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
  
    return ret;
}