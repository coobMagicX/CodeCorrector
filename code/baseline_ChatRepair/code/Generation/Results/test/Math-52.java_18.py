public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();

    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
      throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }

    double v1Norm = FastMath.sqrt(u1u1 / v1v1);
    v1 = new Vector3D(v1Norm, v1);

    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / v1v1;  // Changed from v1v2 / u1u1
    double beta = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    Vector3D u3 = u1.crossProduct(u2);
    Vector3D v3 = v1.crossProduct(v2);
    Vector3D k = u3.crossProduct(v3);

    double c = k.dotProduct(u3.crossProduct(u2));

    if (c == 0) {
        throw MathRuntimeException.createIllegalArgumentException("Specified vectors lead to a degenerate rotation");
    }

    double invC = 1.0 / FastMath.sqrt(c);

    q1 = invC * k.getX();
    q2 = invC * k.getY();
    q3 = invC * k.getZ();

    // Recompute k for accurate scalar part calculation
    k = new Vector3D(u1.getY() * q3 - u1.getZ() * q2,
                     u1.getZ() * q1 - u1.getX() * q3,
                     u1.getX() * q2 - u1.getY() * q1);

    q0 = 0.5 * v1.dotProduct(k) / k.getNorm(); // Adjusted scalar computation
}
