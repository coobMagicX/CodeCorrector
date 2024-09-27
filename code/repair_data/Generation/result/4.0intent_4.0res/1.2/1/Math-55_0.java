public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Normalize v2 to avoid scaling issues
    Vector3D normalizedV2 = v2.normalize();

    // Compute dot product v1.v2 and v2.v2
    double dotV1V2 = v1.dot(normalizedV2);
    double dotV2V2 = normalizedV2.dot(normalizedV2);

    // Compute rho as an approximation of v1.v2 / v2.v2
    double rho = dotV1V2 / dotV2V2;

    // Compute v3 = v1 - rho * v2
    Vector3D v3 = v1.subtract(normalizedV2.scalarMultiply(rho));

    // Compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(
        v3.y * v2.z - v3.z * v2.y,
        v3.z * v2.x - v3.x * v2.z,
        v3.x * v2.y - v3.y * v2.x
    );
}