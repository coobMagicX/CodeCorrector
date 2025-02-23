// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);
double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    throw new ArithmeticException("Normalization factor (norm squared of k) is zero");
}
q0 = vRef.dotProduct(k) / (2 * kNormSq);
