// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);

double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    // The vectors are aligned, it should be an identity rotation or no rotation needed
    q0 = 1.0; // Identity rotation (no rotation)
} else {
    q0 = vRef.dotProduct(k) / (2 * kNormSq);
}
