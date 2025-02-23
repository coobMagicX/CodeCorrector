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
    double u1u2   = u1.dotProduct(u2);
    double v1v2   = v1.dotProduct(v2);
    double coeffU = u1u2 / u1u1;
    double coeffV = v1v2 / u1u1;
    double beta   = Math.sqrt((u2u2 - u1u2 * coeffU) / (v2v2 - v1v2 * coeffV));
    double alpha  = coeffU - beta * coeffV;
    v2 = new Vector3D(alpha, v1, beta, v2);

    // preliminary computation
    Vector3D k     = v1.subtract(u1).crossProduct(v2.subtract(u2));
    double kNormSq = k.getNormSq();
    double u3      = u1.crossProduct(u2).getNorm();
    
    // ensure kNormSq is not zero to avoid division by zero
    double q0;
    if (Math.abs(kNormSq) < 1e-10) {
        // if k is almost zero then the rotation angle should be very small, angle is approximated as 0 
        q0 = 1.0;
    } else {
        // compute the scalar part by including the correct area form product factor
        q0 = v1.crossProduct(v2).subtract(u1.crossProduct(u2)).getNorm();
        q0 /= 2 * Math.sqrt(kNormSq);
    }
    
    double q1 = k.getX() / (2 * q0);
    double q2 = k.getY() / (2 * q0);
    double q3 = k.getZ() / (2 * q0);

    // Normalizing Quaternion for safety, although it should already be normalized
    double norm = Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
    q0 /= norm;
    q1 /= norm;
    q2 /= norm;
    q3 /= norm;

    // Setting the instance variables of the Rotation object
    this.q0 = q0;
    this.q1 = q1;
    this.q2 = q2;
    this.q3 = q3;
}
