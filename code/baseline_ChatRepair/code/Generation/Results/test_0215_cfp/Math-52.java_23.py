// compute the scalar part
double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    throw new ArithmeticException("Norm of vector k is zero, cannot proceed with scalar part computation.");
}
q0 = vRef.dotProduct(k) / (2 * kNormSq);

// Ensure unit quaternion normalization at the end of constructor for safety
double norm = FastMath.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
q0 /= norm;
q1 /= norm;
q2 /= norm;
q3 /= norm;
