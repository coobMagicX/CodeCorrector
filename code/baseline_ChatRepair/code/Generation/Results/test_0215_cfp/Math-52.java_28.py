public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    ...
    // Compute the vectorial part
    c = FastMath.sqrt(c);
    double inv = 1.0 / (c + c);
    q1 = inv * k.getX();
    q2 = inv * k.getY();
    q3 = inv * k.getZ();

    // Ensure we have a valid vector 'k' before computing 'q0'
    if(k.getNormSq() == 0) {
        throw new MathRuntimeException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS);
    }

    // Compute the scalar part, updating 'k' to be perpendicular vector to 'uRef' instead
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);
    double normK = k.getNormSq();
    
    if (normK == 0) {
        // To handle the case where k might still end up zero, choose a non-zero quaternion scalar part
        q0 = 1.0; // This implies a zero rotation around an undefined axis, falling back to identity quaternion
    } else {
        q0 = vRef.dotProduct(k) / (2 * normK);
    }
}
