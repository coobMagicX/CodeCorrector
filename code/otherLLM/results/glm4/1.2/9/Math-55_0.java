public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Compute the dot products with high precision to avoid cancellation errors
    double dotProduct = v1.dot(v2);
    double v2Dotv2 = v2.dot(v2);

    // Compute rho as an 8-bit approximation of v1.v2 / v2.v2, ensuring it does not cause overflow or underflow
    double rho = dotProduct > 0 ? Math.min(dotProduct, Double.MAX_VALUE) : -Math.max(-dotProduct, Double.MIN_VALUE);
    rho /= v2Dotv2;

    // Rescale v1 by (1 - rho), to avoid cancellation errors when computing the cross product
    Vector3D v1_rescaled = new Vector3D(
        v1.x * (1.0 - rho),
        v1.y * (1.0 - rho),
        v1.z * (1.0 - rho)
    );

    // Compute the cross product using the rescaled vector and v2
    return new Vector3D(v1_rescaled.y * v2.z - v1_rescaled.z * v2.y,
                        v1_rescaled.z * v2.x - v1_rescaled.x * v2.z,
                        v1_rescaled.x * v2.y - v1_rescaled.y * v2.x);
}