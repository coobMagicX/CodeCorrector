public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double v1Norm = v1.getNorm();
    double v2Norm = v2.getNorm();
    double rho = (float) (v1.dotProduct(v2) / Math.pow(v2Norm, 2));
    Vector3D v3 = v1.subtract(v2.scalarMultiply(rho));

    // compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(v3.y * v2.z - v3.z * v2.y, v3.z * v2.x - v3.x * v2.z, v3.x * v2.y - v3.y * v2.x);
}