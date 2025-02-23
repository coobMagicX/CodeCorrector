public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Compute the cross product directly from v1 and v2.
    return new Vector3D(
        v1.y * v2.z - v1.z * v2.y, // Compute i-component
        v1.z * v2.x - v1.x * v2.z, // Compute j-component
        v1.x * v2.y - v1.y * v2.x  // Compute k-component
    );
}
