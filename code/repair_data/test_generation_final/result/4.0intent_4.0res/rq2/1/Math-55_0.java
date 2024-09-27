public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // We will assume the normalization process could be causing precision issues in cases
    // of very small or very large magnitude vectors leading to failing tests due to cancellation.
    // Thus, we will revert to calculating the cross product directly from the original vectors.

    // Compute cross product directly from v1 and v2
    return new Vector3D(
        v1.y * v2.z - v1.z * v2.y,
        v1.z * v2.x - v1.x * v2.z,
        v1.x * v2.y - v1.y * v2.x
    );
}