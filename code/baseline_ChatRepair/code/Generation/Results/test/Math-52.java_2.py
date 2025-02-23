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
    double coeffV = v1v2 / u1u1;
    double beta = Math.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    Vector3D u3 = u1.crossProduct(u2);
    Vector3D v3 = v1.crossProduct(v2);
    Vector3D k = u3.crossProduct(v3);

    double c = k.dotProduct(k);  // This should be the square of the norm of k
    if (c == 0) {
        // Fall back identity rotation if vectors are aligned
        q0 = 1.0;
        q1 = q2 = q3 = 0.0;
        return;
    }

    c = Math.sqrt(c);

    // Normalize k and multiply by the correct scalar value for quaternion
    k = k.scalarMultiply(1.0 / c);

    // Compute scalar part
    double dotProd = u1.dotProduct(v1) + u2.dotProduct(v2) + u3.dotProduct(v3);
    q0 = dotProd / (2.0 * c);

    // Assign the computed values to quaternion parts
    q1 = k.getX();
    q2 = k.getY();
    q3 = k.getZ();
}
