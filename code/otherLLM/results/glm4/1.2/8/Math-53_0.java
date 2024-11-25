public class Complex {

    private double real;
    private double imaginary;

    public static Complex createComplex(double real, double imaginary) {
        return new Complex(real, imaginary);
    }

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex add(Complex rhs) throws NullArgumentException {
        MathUtils.checkNotNull(rhs);
        
        // Repair strategy: If either the real or imaginary part is NaN, then the result should be NaN.
        boolean isRealNaN = Double.isNaN(this.real) || Double.isNaN(rhs.getReal());
        boolean isImaginaryNaN = Double.isNaN(this.imaginary) || Double.isNaN(rhs.getImaginary());

        if (isRealNaN || isImaginaryNaN) {
            return Complex.createComplex(Double.NaN, Double.NaN);
        } else {
            return Complex.createComplex(this.real + rhs.getReal(), this.imaginary + rhs.getImaginary());
        }
    }
}