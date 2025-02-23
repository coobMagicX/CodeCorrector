// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);

double kNormSq = k.getNormSq();
if (kNormSq == 0) {
  // Handling the case when the norm of k is zero which means the Quaternion should be identity or no rotation
  q0 = 1.0;
  q1 = 0.0;
  q2 = 0.0;
  q3 = 0.0;
} else {
  q0 = vRef.dotProduct(k) / (2 * kNormSq);
}
