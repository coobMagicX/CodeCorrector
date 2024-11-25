public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Compute the dot product of v1 and v2 to determine a scaling factor (rho)
    double rho = v1.dot(v2) / Math.pow(v2.dot(v2), 0.5);

    // Adjust v1 to minimize cancellation error
    Vector3D v1_scaled = v1.subtract(v2.multiply(rho));

    // Now compute the cross product using the adjusted vector
    return new Vector3D(
        v1_scaled.y * v2.z - v1_scaled.z * v2.y,
        v1_scaled.z * v2.x - v1_scaled.x * v2.z,
        v1_scaled.x * v2.y - v1_scaled.y * v2.x
    );
}