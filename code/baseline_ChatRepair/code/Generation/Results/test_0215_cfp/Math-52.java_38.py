// compute the scalar part
double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    // Handle the case when the vectorial part is zero (Identity rotation or no proper rotation)
    q0 = 1.0;
    q1 = 0.0;
    q2 = 0.0;
    q3 = 0.0;
} else {
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);
    q0 = vRef.dotProduct(k) / (2 * kNormSq);
}
