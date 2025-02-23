// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);
double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    // When k is a zero vector, which can occur if vectors are parallel or anti-parallel
    // using a simple condition to handle this specific case, for parallel or aligned vectors:
    if (uRef.dotProduct(vRef) > 0) {
        q0 = 1.0;
    } else {
        q0 = -1.0; // For antiparallel, representing 180 degree rotation
    }
    q1 = 0.0;
    q2 = 0.0;
    q3 = 0.0;
} else {
    q0 = vRef.dotProduct(k) / (2 * kNormSq);
}
