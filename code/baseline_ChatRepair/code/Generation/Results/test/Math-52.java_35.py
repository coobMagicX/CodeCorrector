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
    v1 = new Vector3D(FastMath.sqrt(u1u1 / v1v1), v1);

    // adjust v2 in order to have (u1|u2) = (v1'|v2') and (v2'|v2') = (u2|u2)
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / u1u1;
    double beta = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    // preliminary computation
    Vector3D k = v1.subtract(u1).crossProduct(v2.subtract(u2));
    double c = k.dotProduct(u1.crossProduct(u2));

    if (c == 0) {
      throw new IllegalArgumentException("Vectors u and v cannot define a unique rotation because they are collinear or zero vector.");
    }

    // compute the vectorial part
    c = FastMath.sqrt(c);
    double inv = 1.0 / (c + c);
    q1 = inv * k.getX();
    q2 = inv * k.getY();
    q3 = inv * k.getZ();

    // compute the scalar part
    k = new Vector3D(u1.getY() * q3 - u1.getZ() * q2,
                     u1.getZ() * q1 - u1.getX() * q3,
                     u1.getX() * q2 - u1.getY() * q1);
    q0 = v1.dotProduct(k) / (2 * k.getNormSq());

    if (Double.isNaN(q0)) {
        throw new ArithmeticException("Invalid scalar part computed as NaN due to division by zero or other numerical error.");
    }
}
