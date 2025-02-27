public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    if ((u1u1 == 0) || (u2u2 == 0) || (v1v1 == 0) || (v2v2 == 0)) {
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR);
    }

    v1 = new Vector3D(FastMath.sqrt(u1u1 / v1v1), v1);
    ...
    Vector3D k = v1Su1.crossProduct(v2Su2);
    Vector3D u3 = u1.crossProduct(u2);
    double c = k.dotProduct(u3);

    if (c == 0) {
        Vector3D v3 = Vector3D.crossProduct(v1, v2);
        Vector3D v3Su3 = v3.subtract(u3);
        k = v1Su1.crossProduct(v3Su3);
        Vector3D u2Prime = u1.crossProduct(u3);
        c = k.dotProduct(u2Prime);

        if (c == 0) {
            k = v2Su2.crossProduct(v3Su3);
            c = k.dotProduct(u2.crossProduct(u3));

            if (c == 0) {
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

    c = FastMath.sqrt(c);
    double inv = 1.0 / (c + c);
    q1 = inv * k.getX();
    q2 = inv * k.getY();
    q3 = inv * k.getZ();

    k = new Vector3D(uRef.getY() * q3 - uRef.getZ() * q2, uRef.getZ() * q1 - uRef.getX() * q3, uRef.getX() * q2 - uRef.getY() * q1);
    double kNormSq = k.getNormSq();
    if (kNormSq != 0) {
        q0 = vRef.dotProduct(k) / (2 * kNormSq);
    } else {
        q0 = 1.0; // This might need domain-specific handling based on requirements.
    }
}
