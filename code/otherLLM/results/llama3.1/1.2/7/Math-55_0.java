public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {

    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double scale = Math.max(Math.abs(v1.x), Math.abs(v2.x));
    Vector3D scaledV1 = new Vector3D(v1.x / scale, v1.y / scale, v1.z / scale);
    Vector3D scaledV2 = new Vector3D(v2.x / scale, v2.y / scale, v2.z / scale);

    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf

    double rho = scaledV1.dotProduct(scaledV2) / (scaledV2.dotProduct(scaledV2));
    Vector3D v3 = new Vector3D(scaledV1.x - rho * scaledV2.x, scaledV1.y - rho * scaledV2.y, scaledV1.z - rho * scaledV2.z);

    // compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(v3.y * scaledV2.z - v3.z * scaledV2.y, v3.z * scaledV2.x - v3.x * scaledV2.z, v3.x * scaledV2.y - v3.y * scaledV2.x);
}

public static double dotProduct(Vector3D v1, Vector3D v2) {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
}