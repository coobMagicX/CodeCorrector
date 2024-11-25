public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {

    // Check if either of the input vectors is a zero vector
    if (v1.getNorm() == 0 || v2.getNorm() == 0) {
        return new Vector3D(0, 0, 0);
    }

    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude

    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf

    // compute rho as an 8 bits approximation of v1.v2 / v2.v2
    double rho = (v1.dotProduct(v2) / v2.dotProduct(v2));

    Vector3D scaledV1 = scalarMultiply(rho, v2);
    Vector3D v3 = subtract(scaledV1, v1);

    // compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(
            v3.y * v2.z - v3.z * v2.y,
            v3.z * v2.x - v3.x * v2.z,
            v3.x * v2.y - v3.y * v2.x);
}