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
    v1 = new Vector3D(Math.sqrt(u1u1 / v1v1), v1);

    // adjust v2 in order to have (u1|u2) = (v1'|v2') and (v2'|v2') = (u2|u2)
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / v1v1;  // corrected denominator to v1v1
    double beta = Math.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    // compute quaternion from the two pairs of vectors
    Vector3D u = u1.crossProduct(u2);
    Vector3D v = v1.crossProduct(v2);
    double uDotU = u.getNormSq();
    double vDotV = v.getNormSq();

    Vector3D w = u.crossProduct(v);
    double wDotW = w.getNormSq();

    if (wDotW == 0) { // vectors are parallel
        // identity rotation since the directions are aligned
        q0 = 1.0;
        q1 = 0.0;
        q2 = 0.0;
        q3 = 0.0;
    } else {
        double dot = u.dotProduct(v);
        q0 = Math.sqrt(uDotU * vDotV) + dot; // quaternion scalar part
        q1 = w.getX();
        q2 = w.getY();
        q3 = w.getZ();

        // Normalize quaternion
        double qLength = Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
        q0 /= qLength;
        q1 /= qLength;
        q2 /= qLength;
        q3 /= qLength;
    }
}
