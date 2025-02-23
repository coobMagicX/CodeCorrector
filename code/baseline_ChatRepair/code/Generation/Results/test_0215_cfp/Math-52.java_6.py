public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {

    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
      throw new IllegalArgumentException("Zero norm for rotation defining vector");
    }

    // normalization of vectors
    v1 = v1.normalize();
    v2 = v2.normalize();
    u1 = u1.normalize();
    u2 = u2.normalize();

    // Cross and dot products to use in rotation calculation
    Vector3D crossU1U2 = u1.crossProduct(u2);
    Vector3D crossV1V2 = v1.crossProduct(v2);

    // Rotation axis:
    Vector3D k = crossU1U2.crossProduct(crossV1V2);

    // Compute the quaternion
    double sinTheta = crossU1U2.getNorm() * crossV1V2.getNorm();
    double cosTheta = crossU1U2.dotProduct(crossV1V2);
    double angle = Math.atan2(sinTheta, cosTheta);
    
    double c = k.getNorm();
    if (c == 0) {
      throw new IllegalArgumentException("Rotation axis magnitude is zero, possibly input vectors are linearly dependent");
    }

    double invC = 1.0 / c;
    q1 = invC * k.getX();
    q2 = invC * k.getY();
    q3 = invC * k.getZ();

    k = new Vector3D(u1.getY() * q3 - u1.getZ() * q2,
                     u1.getZ() * q1 - u1.getX() * q3,
                     u1.getX() * q2 - u1.getY() * q1);

    q0 = Math.cos(angle / 2.0);
    if (u1.dotProduct(k) < 0) { // Adjusting the sign of q0
        q0 = -q0;
    }
}
