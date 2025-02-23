// Compute the vectorial part
c = FastMath.sqrt(c);
if (c == 0) {
    q0 = 1.0;
    q1 = 0.0;
    q2 = 0.0;
    q3 = 0.0;
} else {
    double invC = 1.0 / (c + c);
    q1 = invC * k.getX();
    q2 = invC * k.getY();
    q3 = invC * k.getZ();

    // Compute the scalar part using a epsilon to prevent division-by-zero
    double kNormSq = k.getNormSq();
    if (Math.abs(kNormSq) < 1.0e-10) {  // a small threshold close to zero; adjust if necessary
        q0 = 0;  // Set as zero or compute an alternate safe value
    } else {
        k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                         uRef.getZ() * q1 - uRef.getX() * q3,
                         uRef.getX() * q2 - uRef.getY() * q1);
        q0 = vRef.dotProduct(k) / (2 * kNormSq);
    }
}
