public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double normV1 = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    double normV2 = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
    double scaleFactor = Math.max(normV1, normV2);
    Vector3D scaledV1 = new Vector3D(v1.x / scaleFactor, v1.y / scaleFactor, v1.z / scaleFactor);
    Vector3D scaledV2 = new Vector3D(v2.x / scaleFactor, v2.y / scaleFactor, v2.z / scaleFactor);

    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf
    double dotProductV1V2 = scaledV1.x * scaledV2.x + scaledV1.y * scaledV2.y + scaledV1.z * scaledV2.z;
    double dotProductV2V2 = scaledV2.x * scaledV2.x + scaledV2.y * scaledV2.y + scaledV2.z * scaledV2.z;
    double rho = dotProductV1V2 / dotProductV2V2;
    Vector3D v3 = new Vector3D(scaledV1.x - rho * scaledV2.x, scaledV1.y - rho * scaledV2.y, scaledV1.z - rho * scaledV2.z);

    // compute cross product from v3 and v2 instead of v1 and v2
    // Recompute the cross product with non-scaled vectors for improved accuracy after preconditioning
    return new Vector3D(
        v3.y * v2.z - v3.z * v2.y,
        v3.z * v2.x - v3.x * v2.z,
        v3.x * v2.y - v3.y * v2.x
    );
}