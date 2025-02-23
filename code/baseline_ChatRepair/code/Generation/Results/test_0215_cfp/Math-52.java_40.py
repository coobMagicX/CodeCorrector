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
    v1 = v1.scalarMultiply(Math.sqrt(u1u1 / v1v1));

    // adjust v2
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / u1u1;
    double beta = Math.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = v1.scalarMultiply(alpha).add(v2.scalarMultiply(beta));

    // compute the orthogonal vector via cross product and the scalar part
    Vector3D k = v1.subtract(u1).crossProduct(v2.subtract(u2));
    Vector3D u3 = u1.crossProduct(u2);

    double c = k.dotProduct(u3);

    if (c == 0) {
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.INVALID_ROTATION_MATRIX);
    }

    // compute the scalar part
    c = Math.sqrt(c);
    double inv = 1.0 / (c * 2);
    double q1 = inv * k.getX();
    double q2 = inv * k.getY();
    double q3 = inv * k.getZ();

    // compute k again for q0 calculation
    k = new Vector3D(u3.getY() * q2 - u3.getZ() * q1,
                     u3.getZ() * q1 - u3.getX() * q3,
                     u3.getX() * q2 - u3.getY() * q1);

    double kNormSq = k.getNormSq();
    if (kNormSq == 0) {
      throw MathRuntimeException.createIllegalArgumentException();
    }

    q0 = (u1.add(u2)).dotProduct(k) / (kNormSq * 2);
}
