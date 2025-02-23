// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);

double kNormSq = k.getNormSq(); // Get the squared norm of vector k.

if (kNormSq == 0) {
    q0 = 1.0;  // This assumes the identity rotation when k is zero or degenerate.
} else {
    q0 = vRef.dotProduct(k) / (2 * kNormSq);  // This considers the non-zero norm case.
}
