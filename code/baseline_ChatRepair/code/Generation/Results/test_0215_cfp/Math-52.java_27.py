public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // Existing code...
    
    // Compute the vectorial part
    c = FastMath.sqrt(c);
    double inv = 1.0 / (c + c);
    q1 = inv * k.getX();
    q2 = inv * k.getY();
    q3 = inv * k.getZ();

    // Compute the scalar part
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);
    double kNormSq = k.getNormSq();

    if (kNormSq == 0) {
        // Handle case where kNormSq is zero to avoid division by zero
        q0 = 0.0;
    } else {
        q0 = vRef.dotProduct(k) / (2 * kNormSq);
    }

    // Additional code...
}
