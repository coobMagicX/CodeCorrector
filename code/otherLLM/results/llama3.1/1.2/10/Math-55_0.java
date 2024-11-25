public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {

    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude

    double rho = computeRho(v1, v2);
    Vector3D v3 = subtractScaledVector(v1, rho, v2);

    // compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(
            v3.y * v2.z - v3.z * v2.y,
            v3.z * v2.x - v3.x * v2.z,
            v3.x * v2.y - v3.y * v2.x);

}

private static double computeRho(final Vector3D v1, final Vector3D v2) {
    return (v1.dotProduct(v2)) / (v2.dotProduct(v2));
}

private static Vector3D subtractScaledVector(final Vector3D v1, final double rho, final Vector3D v2) {
    return new Vector3D(
            v1.x - rho * v2.x,
            v1.y - rho * v2.y,
            v1.z - rho * v2.z);
}

private static double dotProduct(final Vector3D v1, final Vector3D v2) {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
}