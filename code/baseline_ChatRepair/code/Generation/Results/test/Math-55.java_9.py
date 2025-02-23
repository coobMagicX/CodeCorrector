public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Preconditioning not implemented due to lack of required derivative computation as input.
    // The simple direct accurate computation method is as follows:

    final double rho = (v1.dotProduct(v2)) / (v2.dotProduct(v2));
    
    // Compute v3 = v1 - rho * v2
    Vector3D v3 = new Vector3D(1, v1.x - rho * v2.x,
                                  v1.y - rho * v2.y,
                                  v1.z - rho * v2.z);

    // Compute the cross product from v3 and v2
    return new Vector3D(v3.y * v2.z - v3.z * v2.y,
                        v3.z * v2.x - v3.x * v2.z,
                        v3.x * v2.y - v3.y * v2.x);
}
