if (k.getNormSq() == 0) {
    // If k's norm is zero, it indicates a situation causing division by zero,
    // Usually identity rotation or complete alignment, set quaternion to no rotation.
    q0 = 1.0;
    q1 = 0.0;
    q2 = 0.0;
    q3 = 0.0;
} else {
    // compute the scalar part
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);
    q0 = vRef.dotProduct(k) / (2 * k.getNormSq());
}
