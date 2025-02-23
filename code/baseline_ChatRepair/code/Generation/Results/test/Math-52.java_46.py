// Compute the scalar part:
// Ensure k.getNormSq() does not return zero to prevent division by zero.
double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    // This scenario corresponds to a degenerate cross product resulting in zero magnitude.
    // Handle edge case here, possibly by setting default quaternion (representing no rotation).
    q0 = 1.0;
    q1 = 0.0;
    q2 = 0.0;
    q3 = 0.0;
} else {
    double q0_numerator = vRef.dotProduct(k);
    double q0_denominator = 2 * kNormSq;
    
    // Check if the denominator is very small to avoid precision issues.
    if (Math.abs(q0_denominator) < 1e-10) {
        throw new ArithmeticException("Denominator too small leading to potential precision error.");
    }

    q0 = q0_numerator / q0_denominator;

    // Potentially normalize the quaternion to ensure q0^2 + q1^2 + q2^2 + q3^2 = 1.
    double norm = Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
    q0 /= norm;
    q1 /= norm;
    q2 /= norm;
    q3 /= norm;
}

// Make sure the quaternion is properly normalized or handle the specific edge case:
if(Double.isNaN(q0) || Double.isNaN(q1) || Double.isNaN(q2) || Double.isNaN(q3)) {
    // Fallback to no rotation if normalization fails.
    q0 = 1.0;
    q1 = 0.0;
    q2 = 0.0;
    q3 = 0.0;
}
