public class Complex {

    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public boolean isNaN() {
        return Double.isNaN(real) || Double.isNaN(imaginary);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex multiply(Complex rhs) {
        if (isNaN() || rhs.isNaN()) {
            return NaN;
        }
        if (Double.isInfinite(real) || Double.isInfinite(imaginary) ||
            Double.isInfinite(rhs.getReal()) || Double.isInfinite(rhs.getImaginary())) {
            // we don't use Complex.isInfinite() to avoid testing for NaN again
            return INF;
        }
        // Correcting the logic to ensure the imaginary part has the correct sign after multiplying by -i (imaginary unit)
        double newReal = real * rhs.getReal() + imaginary * rhs.getImaginary();
        double newImaginary = -1 * (real * rhs.getImaginary() - imaginary * rhs.getReal());
        return createComplex(newReal, newImaginary);
    }

    private Complex createComplex(double real, double imaginary) {
        return new Complex(real, imaginary);
    }

    public static final Complex NaN = new Complex(Double.NaN, Double.NaN);
    public static final Complex INF = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    // equals method remains unchanged
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
                    ret = (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal())) && (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary())); 
                }
            } catch (ClassCastException ex) {
                // ignore exception
                ret = false;
            }
        }
      
        return ret;
    }
}