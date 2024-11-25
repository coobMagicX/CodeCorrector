public class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public static Complex createComplex(Double realPart, Double imagPart) {
        // Check if either part is NaN and return a Complex with both parts as NaN
        if (realPart != null && Double.isNaN(realPart) || imagPart != null && Double.isNaN(imagPart)) {
            return new Complex(Double.NaN, Double.NaN);
        }
        return new Complex(realPart, imagPart); // Normal case
    }

    public static void checkNotNull(Object object) throws NullArgumentException {
        if (object == null) {
            throw new NullArgumentException("Object cannot be null.");
        }
    }

    public Complex add(Complex rhs) throws NullArgumentException {
        MathUtils.checkNotNull(rhs);
        Double realSum = createComplex(real, rhs.getReal()).getReal();
        Double imagSum = createComplex(imaginary, rhs.getImaginary()).getImaginary();
        return createComplex(realSum, imagSum);
    }

    // Additional methods like getters and setters are omitted for brevity
}

class NullArgumentException extends Exception {
    public NullArgumentException(String message) {
        super(message);
    }
}

// Assuming MathUtils class exists with a checkNotNull method that throws NullArgumentException