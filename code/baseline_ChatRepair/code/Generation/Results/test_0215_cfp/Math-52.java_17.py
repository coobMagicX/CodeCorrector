public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // Same preamble code...

    // compute the vectorial part
    c = FastMath.sqrt(c);
    double inv = 1.0 / (c + c);
    q1 = inv * k.getX();
    q2 = inv * k.getY();
    q3 = inv * k.getZ();

    // Adjust computation for calculating q0
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);

    double kNormSq = k.getNormSq();
    if (Math.abs(kNormSq) < 1e-20) {
        // Avoid dividing by a very small number that leads to instability or NaN
        q0 = 1.0; // Assume no rotation (uRef and vRef are effectively aligned)
    } else {
        q0 = vRef.dotProduct(k) / (2 * kNormSq);
    }
}
