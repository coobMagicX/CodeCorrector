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
double u1u2   = u1.dotProduct(u2);
double v1v2   = v1.dotProduct(v2);
double coeffU = u1u2 / u1u1;
double coeffV = v1v2 / u1u1;
double beta   = FastMath.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
double alpha  = coeffU - beta * coeffV;
v2 = new Vector3D(alpha, v1, beta, v2);

// preliminary computation
Vector3D uRef  = u1;
Vector3D vRef  = v1;
Vector3D v1Su1 = v1.subtract(u1);
Vector3D v2Su2 = v2.subtract(u2);
Vector3D k     = crossProduct(v1Su1, v2Su2);
Vector3D u3    = crossProduct(u1, u2);
double c       = dotProduct(k, u3);
if (c == 0) {
  // the (q1, q2, q3) vector is close to the (u1, u2) plane
  // we try other vectors
  Vector3D v3 = crossProduct(v1, v2);
  Vector3D v3Su3 = v3.subtract(u3);
  k = crossProduct(v1Su1, v3Su3);
  Vector3D u2Prime = crossProduct(u1, u3);
  c = dotProduct(k, u2Prime);

  if (c == 0) {
    // the (q1, q2, q3) vector is also close to the (u1, u3) plane,
    // it is almost aligned with u1: we try (u2, u3) and (v2, v3)
    k = crossProduct(v2Su2, v3Su3);;
    c = dotProduct(k, crossProduct(u2, u3));;

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

// normalize k vector
k.normalize();

// compute the vectorial part
c = FastMath.sqrt(c);
double inv = 1.0 / (c + c);
q1 = inv * k.getX();
q2 = inv * k.getY();
q3 = inv * k.getZ();

// compute the scalar part
 k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2,
                  uRef.getZ() * q1 - uRef.getX() * q3,
                  uRef.getX() * q2 - uRef.getY() * q1);
q0 = vRef.dotProduct(k) / (2 * k.getNormSq());

}

private Vector3D crossProduct(Vector3D v1, Vector3D v2) {
  return new Vector3D(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                       v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
                       v1.getX() * v2.getY() - v1.getY() * v2.getX());
}

private double dotProduct(Vector3D v1, Vector3D v2) {
  return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
}