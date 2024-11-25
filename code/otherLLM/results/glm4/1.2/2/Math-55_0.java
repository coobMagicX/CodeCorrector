public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Ensure that the result of v1 and v2 is not zero to avoid division by zero
    if (v2.dot(v2) == 0.0) {
        throw new IllegalArgumentException("Second vector cannot be the zero vector.");
    }

    // Compute rho as an 8-bit approximation of v1.v2 / v2.v2 to prevent loss of precision
    double rho = v1.dot(v2) / (v2.dot(v2));

    // Create a temporary vector to hold the rescaled version of v1
    Vector3D v3 = new Vector3D();
    v3.x = v1.x - rho * v2.x;
    v3.y = v1.y - rho * v2.y;
    v3.z = v1.z - rho * v2.z;

    // Compute the cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(v3.y * v2.z - v3.z * v2.y, v3.z * v2.x - v3.x * v2.z, v3.x * v2.y - v3.y * v2.x);
}