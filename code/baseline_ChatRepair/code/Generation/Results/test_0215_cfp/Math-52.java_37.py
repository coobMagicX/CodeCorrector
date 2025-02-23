// compute the scalar part
k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                 uRef.getZ() * q1 - uRef.getX() * q3,
                 uRef.getX() * q2 - uRef.getY() * q1);
double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    // If the vector part k is a zero vector, set q0 to 1, representing no rotation (identity quaternion)
    q0 = 1.0;
} else {
    // Otherwise, compute q0 normally
    q0 = vRef.dotProduct(k) / (2 * kNormSq);
}

// additional error handling or normalization may be required after this point
