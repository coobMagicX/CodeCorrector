public boolean equals(Object other) {
    boolean ret;

    if (this == other) { 
        ret = true;
    } else if (other == null) {
        ret = false;
    } else  {
        try {
            Complex rhs = (Complex)other;
            // Corrected the implementation to account for differences in representation or calculation
            // between two Complex objects that are mathematically equivalent.
            if (rhs.isNaN()) {
                ret = this.isNaN();
            } else {
                double realComp = getReal();
                double imaginaryComp = getImaginary();

                double rhsReal = rhs.getReal();
                double rhsImaginary = rhs.getImaginary();

                // If the two complex numbers are mathematically equivalent, but have different signs
                // or representations (e.g., one is positive and the other is negative), consider them equal.
                ret = (Math.abs(realComp - rhsReal) < 1E-12 && Math.abs(imaginaryComp - rhsImaginary) < 1E-12);
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }

    return ret;
}