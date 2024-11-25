public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Normalize both vectors to avoid loss of precision in the dot product calculation.
    double normV1 = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    double normV2 = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);

    Vector3D normalizedV1 = new Vector3D(v1.x / normV1, v1.y / normV1, v1.z / normV1);
    Vector3D normalizedV2 = new Vector3D(v2.x / normV2, v2.y / normV2, v2.z / normV2);

    // Compute the dot product of normalized vectors.
    double dotProduct = normalizedV1.dot(normalizedV2);

    // Calculate rho to minimize cancellation errors.
    double rho = 0.0;
    if (Math.abs(dotProduct) > 1e-8) {
        rho = Math.cbrt(3.0 * Math.pow(Math.abs(dotProduct), 2) / Math.pow(normV2, 2));
    }

    // Precondition v1 to reduce cancellation errors.
    Vector3D v3 = new Vector3D(normalizedV1.x - rho * normalizedV2.x,
                                normalizedV1.y - rho * normalizedV2.y,
                                normalizedV1.z - rho * normalizedV2.z);

    // Compute the cross product of v3 and v2 instead of original v1 and v2.
    return new Vector3D(v3.y * v2.z - v3.z * v2.y, v3.z * v2.x - v3.x * v2.z, v3.x * v2.y - v3.y * v2.x);
}