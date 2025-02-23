public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double v2NormSq = v2.x * v2.x + v2.y * v2.y + v2.z * v2.z;
    double dotProduct = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    double rho = dotProduct / v2NormSq;

    // Create v3 as v1 - rho * v2
    Vector3D v3 = new Vector3D(v1.x - rho * v2.x, v1.y - rho * v2.y, v1.z - rho * v2.z);

    // Compute cross product of v3 and v2
    return new Vector3D(
        v3.y * v2.z - v3.z * v2.y,
        v3.z * v2.x - v3.x * v2.z,
        v3.x * v2.y - v3.y * v2.x
    );
}
