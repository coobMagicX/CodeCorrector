public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {

    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
      throw new IllegalArgumentException("Zero norm for rotation defining vector");
    }

    // normalize v1 to have (v1'|v1') = (u1|u1)
    v1 = v1.scalarMultiply(Math.sqrt(u1u1 / v1v1));

    // adjust v2 to have (u1|u2) = (v1'|v2') and (v2'|v2') = (u2|u2)
    double u1u2 = u1.dotProduct(u2);
    double v1v2 = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / v1v1;
    double beta = Math.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha = coeffU - beta * coeffV;
    v2 = v1.scalarMultiply(alpha).add(v2.scalarMultiply(beta));

    Vector3D uRef = u1;
    Vector3D vRef = v1;
    Vector3D v1Su1 = v1.subtract(u1);
    Vector3D v2Su2 = v2.subtract(u2);
    Vector3D k = v1Su1.crossProduct(v2Su2);
    Vector3D u3 = u1.crossProduct(u2);
    double c = k.dotProduct(u3);

    if (Math.abs(c) < 1e-12) {
      Vector3D v3 = v1.crossProduct(v2);
      Vector3D v3Su3 = v3.subtract(u3);
      k = v1Su1.crossProduct(v3Su3);
      Vector3D u2Prime = u1.crossProduct(u3);
      c = k.dotProduct(u2Prime);

      if (Math.abs(c) < 1e-12) {
        k = v2Su2.crossProduct(v3Su3);
        c = k.dotProduct(u2.crossProduct(u3));

        if (Math.abs(c) < 1e-12) {
          // Identity rotation
          q0 = 1.0;
          q1 = 0.0;
          q2 = 0.0;
          q3 = 0.0;
          return;
        }
        uRef = u2;
        vRef = v2;
      }
    }

    // compute the vectorial part 
    c = Math.sqrt(c);
    double inv = 1.0 / (c * 2);
    q1 = inv * k.getX();
    q2 = inv * k.getY();
    q3 = inv * k.getZ();

    // compute the scalar part
    k = new Vector3D(
        uRef.getY() * q3 - uRef.getZ() * q2,
        uRef.getZ() * q1 - uRef.getX() * q3,
        uRef.getX() * q2 - uRef.getY() * q1
    );
    double normK = k.getNorm();
    if (normK == 0) {
      q0 = 1.0;
    } else {
      q0 = vRef.dotProduct(k) / (normK * normK);  
    }
}
