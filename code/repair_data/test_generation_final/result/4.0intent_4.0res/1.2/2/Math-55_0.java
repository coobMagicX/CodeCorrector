public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {

    // Normalizing vectors to ensure their norm are the same order of magnitude
    Vector3D normalizedV1 = v1.normalize();
    Vector3D normalizedV2 = v2.normalize();

    // Reducing cancellation errors by preconditioning,
    // We replace v1 by v3 = v1 - rho * v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf
    double rho = normalizedV1.dot(normalizedV2) / normalizedV2.dot(normalizedV2);
    Vector3D v3 = normalizedV1.subtract(normalizedV2.scalarMultiply(rho));

    // Compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(
        v3.y * normalizedV2.z - v3.z * normalizedV2.y,
        v3.z * normalizedV2.x - v3.x * normalizedV2.z,
        v3.x * normalizedV2.y - v3.y * normalizedV2.x);
}