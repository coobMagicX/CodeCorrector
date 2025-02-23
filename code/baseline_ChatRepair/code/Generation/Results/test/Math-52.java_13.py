public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {

    // ... (earlier code) ...

    // compute the scalar part
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                        uRef.getZ() * q1 - uRef.getX() * q3,
                        uRef.getX() * q2 - uRef.getY() * q1);

    double kNormSq = k.getNormSq();
    if (kNormSq == 0) {
        throw new ArithmeticException("Division by zero while computing q0 in Rotation.");
    }

    q0 = vRef.dotProduct(k) / (2 * kNormSq);

    // Normalize the quaternion to ensure its norm is 1
    double norm = Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
    if (norm == 0) {
        throw new ArithmeticException("Zero norm quaternion.");
    }

    q0 /= norm;
    q1 /= norm;
    q2 /= norm;
    q3 /= norm;

    // Final return condition is implicit as constructors do not return values in Java
}
