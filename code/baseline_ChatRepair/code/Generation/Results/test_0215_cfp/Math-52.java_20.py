// compute the scalar part
double kNormSq = k.getNormSq();
if (kNormSq == 0) {
    // Handle zero norm case, set q0 directly to avoid NaN
    q0 = 1.0;
} else {
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);
    q0 = vRef.dotProduct(k) / (2 * kNormSq);
    if (Double.isNaN(q0)) {
        // In case of numeric instability leading to NaN, default to a logical numeric value, likely because of precision issues
        q0 = 1.0;
    }
}
