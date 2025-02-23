public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }

    // normalize v1 in order to have (v1'|v1') = (u1|u1)
    double v1Factor = FastMath.sqrt(u1u1 / v1v1);
    v1 = new Vector3D(v1Factor, v1);

    // adjust v2 in order to have (u1|u2) = (v1'|v2') and (v2'|v2') = (u2|u2)
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double v1v2Factor = v1v2 / v1v1;
    double beta = FastMath.sqrt((u2u2 - u1u2 * u1u2 / u1u1) / (v2v2 - v1v2Factor * v1v2));
    double alpha = u1u2 / u1u1 - beta * v1v2Factor;
    v2 = new Vector3D(alpha, v1, beta, v2);

    // compute the quaternion
    Vector3D u1Xu2 = u1.crossProduct(u2);
    Vector3D v1Xv2 = v1.crossProduct(v2);
    Vector3D k = u1Xu2.crossProduct(v1Xv2);

    double dot = k.dotProduct(u1Xu2);
    double kNormSq = k.getNormSq();
    if (Math.abs(dot) <= 1.0e-15 * kNormSq) {
        q0 = 1.0;
        q1 = 0.0;
        q2 = 0.0;
        q3 = 0.0;
    } else {
        c = FastMath.sqrt(dot);
        double inv = 1.0 / (c + c);
        q1 = inv * k.getX();
        q2 = inv * k.getY();
        q3 = inv * k.getZ();
        q0 = v1Xv2.dotProduct(k) / (2 * kNormSq);
    }
}
