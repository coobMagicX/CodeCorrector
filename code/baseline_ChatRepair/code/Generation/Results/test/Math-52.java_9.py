// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);

double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    throw new MathRuntimeException(LocalizedFormats.ARITHMETIC_EXCEPTION);
}

q0 = vRef.dotProduct(k) / (2 * kNormSq);

// Normalization of the quaternion, to assure the scalar part (q0) is non-negative which is typical in many quaternion conventions
if (q0 < 0) {
    q0 = -q0;
    q1 = -q1;
    q2 = -q2;
    q3 = -q3;
}
