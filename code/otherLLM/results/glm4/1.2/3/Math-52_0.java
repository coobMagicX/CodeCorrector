public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
        throw new MathRuntimeException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }

    // normalize v1 in order to have (v1'|v1') = (u1|u1)
    v1 = u1.scale(FastMath.sqrt(u1u1 / v1v1));

    // adjust v2 in order to have (u1|u2) = (v1'|v2') and (v2'|v2') = (u2|u2)
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / u1u1;
    double beta = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    // preliminary computation
    Vector3D uRef  = u1;
    Vector3D vRef  = v1;
    Vector3D v1Su1 = v1.subtract(u1);
    Vector3D v2Su2 = v2.subtract(u2);
    Vector3D k     = v1Su1.crossProduct(v2Su2);
    double c       = k.dotProduct(u1.crossProduct(u2));
    if (c == 0) {
        // the (q1, q2, q3) vector is close to the (u1, u2) plane
        // we try other vectors
        Vector3D v3 = u1.crossProduct(u2);
        Vector3D v3Su3 = v3.subtract(u1.crossProduct(u2));
        k = v1Su1.crossProduct(v3Su3);
        c = k.dotProduct(v3);

        if (c == 0) {
            // the (q1, q2, q3) vector is also close to the (u1, u3) plane,
            // it is almost aligned with u1: we try (u2, u3) and (v2, v3)
            k = v2Su2.crossProduct(v3Su3);
            c = k.dotProduct(u2.crossProduct(u3));

            if (c == 0) {
                // the (q1, q2, q3) vector is aligned with everything
                // this is really the identity rotation
                q0 = 1.0;
                q1 = 0.0;
                q2 = 0.0;
                q3 = 0.0;
                return;
            }

            // we will have to use u2 and v2 to compute the scalar part
            uRef = u2;
            vRef = v2;

        }
    }

    // compute the vectorial part
    c = FastMath.sqrt(c);
    double invC = 1.0 / (c + c); // Compute inverse of the magnitude once and use it for q1, q2, q3.
    q1 = invC * k.getX();
    q2 = invC * k.getY();
    q3 = invC * k.getZ();

    // compute the scalar part
    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                     uRef.getZ() * q1 - uRef.getX() * q3,
                     uRef.getX() * q2 - uRef.getY() * q1);
    double normK = k.getNormSq();
    if (normK == 0) {
        // Avoid division by zero
        q0 = 1.0;
    } else {
        q0 = vRef.dotProduct(k) / (2 * FastMath.sqrt(normK));
    }
}