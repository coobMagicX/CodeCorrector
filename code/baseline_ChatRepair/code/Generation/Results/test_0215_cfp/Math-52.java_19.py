public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();

    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }

    // normalize v1
    v1 = new Vector3D(FastMath.sqrt(u1u1 / v1v1), v1);

    // adjust v2
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / u1u1;
    double beta = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    // computations
    Vector3D k = v1.subtract(u1).crossProduct(v2.subtract(u2));
    Vector3D u3 = u1.crossProduct(u2);
    double c = k.dotProduct(u3);

    if (c == 0.0) {
        // handle the case where c is zero
        k = new Vector3D(1.0, u2);  // example fallback vector
        c = k.dotProduct(u3);
    }

    if (c != 0.0) {
        // compute the quaternion
        double invC = 1.0 / (c + c);
        q1 = invC * k.getX();
        q2 = invC * k.getY();
        q3 = invC * k.getZ();

        k = new Vector3D(u1.getY() * q3 - u1.getZ() * q2,
                         u1.getZ() * q1 - u1.getX() * q3,
                         u1.getX() * q2 - u1.getY() * q1);

        double kNormSq = k.getNormSq();
        if (kNormSq != 0) {
            q0 = v1.dotProduct(k) / (2 * kNormSq);
        } else {
            q0 = 1.0; // This indicates pure rotation or no rotation.
        }
    } else {
        // handle the case where c remains zero
        q0 = 1.0;
        q1 = 0.0;
        q2 = 0.0;
        q3 = 0.0;
    }
}
