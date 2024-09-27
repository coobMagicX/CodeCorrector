public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {

    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude

    double rho = 0.0;
    if (v2.getNorm() != 0) {
        rho = v1.dotProduct(v2) / (v2.getNorm() * v2.getNorm());
    }

    Vector3D v3 = v1.subtract(v2.scalarMultiply(rho));

    // compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(v3.getY() * v2.getZ() - v3.getZ() * v2.getY(), v3.getZ() * v2.getX() - v3.getX() * v2.getZ(), v3.getX() * v2.getY() - v3.getY() * v2.getX());
}