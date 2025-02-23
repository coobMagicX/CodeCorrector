public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {

    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();

    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
      throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }

    // Normalize v1 to match the norm of u1
    v1 = new Vector3D(FastMath.sqrt(u1u1 / v1v1), v1);

    // Compute adjusted v2 as explained previously
    double u1u2   = u1.dotProduct(u2);
    double v1v2   = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / v1v1;
    double beta   = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha  = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    // Computing cross products
    Vector3D k = v1.subtract(u1).crossProduct(v2.subtract(u2));
    double c = k.dotProduct(u1.crossProduct(u2));

    if (Double.isNaN(c) || c == 0) {
      throw new IllegalArgumentException("Vectors are coplanar or invalid for forming a rotation.");
    }

    // Compute the quaternion (q0, q1, q2, q3)
    c = FastMath.sqrt(c);
    double invC = 1.0 / (2 * c);
    q1 = invC * k.getX();
    q2 = invC * k.getY();
    q3 = invC * k.getZ();

    // Compute scalar part q0
    Vector3D correctedK = new Vector3D(u1.getY() * q3 - u1.getZ() * q2,
                                       u1.getZ() * q1 - u1.getX() * q3,
                                       u1.getX() * q2 - u1.getY() * q1);

    q0 = v1.dotProduct(correctedK) / (2 * correctedK.getNormSq());

    if (Double.isNaN(q0)) {
      throw new ArithmeticException("Quaternion scalar part (q0) computation resulted in 'NaN'");
    }
}
