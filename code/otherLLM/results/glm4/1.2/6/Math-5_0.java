public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        // Special case: Reciprocal of zero is undefined in the complex plane.
        // Since we cannot divide by zero, and there's no defined value for infinity in this context,
        // we'll return NaN instead to indicate that division by zero occurred.
        return NaN;
    }

    if (isInfinite) {
        // Infinite times anything should still be infinite. However, the original code
        // has an issue with returning a finite number when real and imaginary are both zero.
        // Since infinity is a special case, we'll treat it in this manner:
        return INFINITY;
    }

    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1. / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        // Check for division by zero before attempting to compute the reciprocal
        if (FastMath.abs(real) > FastMath.abs(imaginary)) {
            double q = imaginary / real;
            double scale = 1. / (imaginary * q + real);
            return createComplex(scale, -scale * q);
        } else {
            // This condition covers when both the numerator and denominator are zero,
            // which would lead to a division by zero error.
            if (FastMath.abs(real) == FastMath.abs(imaginary)) {
                // Since we cannot divide by zero in this case, return NaN
                return NaN;
            }
        }
    }
}