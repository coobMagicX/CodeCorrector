// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);
double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    // If norm is zero, avoid division by zero and directly set q0
    q0 = 1.0; // Since all quaternion parts should be zero, so it represents a zero rotation.
} else {
    q0 = vRef.dotProduct(k) / (2 * kNormSq);
}
