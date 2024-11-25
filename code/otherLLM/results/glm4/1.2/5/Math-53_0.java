public class Complex {

    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static void checkNotNull(Object object) throws NullArgumentException {
        if (object == null) {
            throw new NullArgumentException("Object cannot be null");
        }
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex add(Complex rhs) throws NullArgumentException {
        MathUtils.checkNotNull(rhs);
        if (Double.isNaN(real) || Double.isNaN(rhs.getReal())) {
            return new Complex(Double.NaN, Double.NaN);
        }
        if (Double.isNaN(imaginary) || Double.isNaN(rhs.getImaginary())) {
            return new Complex(Double.NaN, Double.NaN);
        }
        return createComplex(real + rhs.getReal(), imaginary + rhs.getImaginary());
    }

    private static Complex createComplex(double real, double imaginary) {
        return new Complex(real, imaginary);
    }

    public boolean isNaN() {
        return Double.isNaN(real) || Double.isNaN(imaginary);
    }
}