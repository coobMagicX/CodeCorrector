public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // ... existing code ...

    // compute the scalar part
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);
    double kNormSq = k.getNormSq();
    if (kNormSq == 0) {
        throw new MathRuntimeException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS);
    }
    q0 = vRef.dotProduct(k) / (2 * kNormSq);

    // ... existing code ...
}
