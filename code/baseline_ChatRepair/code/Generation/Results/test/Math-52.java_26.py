// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);
double kNormSq = k.getNormSq();

if (kNormSq == 0.0) {
    // If k is a zero vector, we cannot compute q0 normally, 
    // perhaps default to q0 as 1 if k is nearly zero, which implies minimal rotation angle.
    q0 = 1.0;
} else {
    q0 = vRef.dotProduct(k) / (2 * kNormSq);
}
