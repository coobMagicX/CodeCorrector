import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

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

    public double getImaginary() {
        return imaginary;
    }

    public Complex add(Complex rhs) throws NullArgumentException {
        MathUtils.checkNotNull(rhs, "The complex number argument cannot be null");
        return new Complex(real + rhs.getReal(), imaginary + rhs.getImaginary());
    }
}