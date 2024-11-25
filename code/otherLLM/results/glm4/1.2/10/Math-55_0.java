public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Compute the dot products and their squares to find the scaling factor
    double dot = v1.dot(v2);
    double normV2Sq = v2.x * v2.x + v2.y * v2.y + v2.z * v2.z;

    // Avoid division by zero or near-zero values that could cause numerical errors
    if (Math.abs(normV2Sq) < 1e-8) {
        throw new ArithmeticException("Vector v2 has a magnitude of zero, cannot compute cross product.");
    }

    // Compute rho as an 8-bit approximation of dot / normV2Sq to prevent precision loss
    double rho = dot / normV2Sq;

    // Add v2 * rho to v1 to rescale v1 without losing precision
    double x3 = v1.x + rho * v2.y;
    double y3 = v1.y - rho * v2.z;
    double z3 = v1.z + rho * v2.x;

    // Compute the cross product using the rescaled vectors
    return new Vector3D(y3 * v2.z - z3 * v2.y, z3 * v2.x - x3 * v2.z, x3 * v2.y - y3 * v2.x);
}