public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) {
    // norms computation
    double u1u1 = u1.getNormSq();
    double u2u2 = u2.getNormSq();
    double v1v1 = v1.getNormSq();
    double v2v2 = v2.getNormSq();
    
    // Check for zero norm vectors which cannot define rotations
    if (u1u1 == 0 || u2u2 == 0 || v1v1 == 0 || v2v2 == 0) {
        throw new IllegalArgumentException("Zero norm for rotation defining vector");
    }

    // Normalize v1 to match the norm of u1
    v1 = v1.normalize().scalarMultiply(Math.sqrt(u1u1));
    
    // Project v2 into the plane defined by u1 and orthogonally to v1, then normalize
    Vector3D v1Unit = v1.normalize();
    Vector3D v2Projected = v2.subtract(v1Unit.scalarMultiply(v1Unit.dotProduct(v2)));
    double projectedV2Norm = u2.subtract(u1.scalarMultiply(u1.dotProduct(u2) / u1u1)).getNorm();
    v2 = v2Projected.normalize().scalarMultiply(projectedV2Norm);

    // Compute the rotation axis by vector cross product
    Vector3D axis = v1.crossProduct(v2);

    // Compute the angle between (u1, u2) and (v1, v2) using arccos of dot products
    double angle = Math.acos(u1.normalize().dotProduct(v1.normalize())
                           * u2.normalize().dotProduct(v2.normalize()));
    
    // Compute quaternion components   
    double s = Math.sin(angle / 2);
    q0 = Math.cos(angle / 2);
    q1 = axis.getX() * s;
    q2 = axis.getY() * s;
    q3 = axis.getZ() * s;

    // Normalize the quaternion to address accumulation of numerical errors
    double norm = Math.sqrt(q0 * q0 + q1 * q1 + q2 * q2 + q3 * q3);
    q0 /= norm;
    q1 /= norm;
    q2 /= norm;
    q3 /= norm;
}
