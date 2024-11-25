public class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    public Complex add(Complex rhs)
            throws NullArgumentException, IllegalArgumentException {
        MathUtils.checkNotNull(rhs);
        
        if (Double.isNaN(this.real) || Double.isNaN(this.imaginary) ||
            Double.isNaN(rhs.getReal()) || Double.isNaN(rhs.getImaginary())) {
            throw new IllegalArgumentException("Cannot add complex numbers with NaN values.");
        }
        
        return createComplex(real + rhs.getReal(), imaginary + rhs.getImaginary());
    }

    private Complex createComplex(double real, double imaginary) {
        return new Complex(real, imaginary);
    }

    // ... other methods and class implementation ...
}

// Assuming the existence of a MathUtils class with a checkNotNull method:
class MathUtils {

    public static void checkNotNull(Object object) throws NullArgumentException {
        if (object == null) {
            throw new NullArgumentException("Object cannot be null");
        }
    }

    // ... other utility methods ...
}

class NullArgumentException extends Exception {
    public NullArgumentException(String message) {
        super(message);
    }
}