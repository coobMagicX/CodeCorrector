public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {

    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
      throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }

    // normalize v1 to have (v1'|v1') = (u1|u1)
    v1 = new Vector3D(FastMath.sqrt(u1u1 / v1v1), v1);

    // adjust v2
    double u1u2   = u1.dotProduct(u2);
    double v1v2   = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / v1v1;
    double beta   = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha  = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    // Compute cross products and coefficients
    Vector3D u1Su2 = u1.subtract(u2);
    Vector3D v1Sv2 = v1.subtract(v2);
    Vector3D k     = u1Su2.crossProduct(v1Sv2);
    Vector3D normK = new Vector3D(1.0 / k.getNorm(), k);
    double c       = normK.dotProduct(u1.crossProduct(u2));
    
    if (Math.abs(c) < 1e-10) {
      throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.ALMOST_ZERO_NORM_FOR_ROTATION_AXIS);
    }

    double invC = 1.0 / c;
    q1 = invC * k.getX();
    q2 = invC * k.getY();
    q3 = invC * k.getZ();

    // compute the scalar part
    Vector3D kOrtho = new Vector3D(u2.getY() * q3 - u2.getZ() * q2,
                                   u2.getZ() * q1 - u2.getX() * q3,
                                   u2.getX() * q2 - u2.getY() * q1);
    double kn2 = kOrtho.getNormSq();
    if (kn2 == 0) {
      throw MathRuntimeException.createArithmeticException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS);
    }

    q0 = v2.dotProduct(kOrtho) / (2.0 * kn2);
}
