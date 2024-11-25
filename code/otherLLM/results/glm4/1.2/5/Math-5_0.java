public Complex reciprocal() {
    // Check for NaN case and zero complex number
    if (isNaN || (real == 0.0 && imaginary == 0.0)) {
        return INFINITE; // Return infinite instead of NaN for a zero complex number
    }

    // Check for infinite magnitude
    if (isInfinite) {
        return ZERO;
    }

    // Choose the correct formula based on which part has a larger absolute value
    double q, scale;
    Complex result;

    if (FastMath.abs(real) >= FastMath.abs(imaginary)) {
        q = imaginary / real;
        scale = 1.0 / (imaginary * q + real);
        result = createComplex(scale, -scale * q);
    } else {
        q = real / imaginary;
        scale = 1.0 / (real * q + imaginary);
        result = createComplex(scale * q, -scale);
    }

    return result; // Return the correct complex number
}