public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double rho = (v1.dotProduct(v2)) / (v2.dotProduct(v2));
    Vector3D v3 = v1.subtract(v2.scalarMultiply(rho));
    return new Vector3D(v3.getY() * v2.getZ() - v3.getZ() * v2.getY(),
                        v3.getZ() * v2.getX() - v3.getX() * v2.getZ(),
                        v3.getX() * v2.getY() - v3.getY() * v2.getX());
}