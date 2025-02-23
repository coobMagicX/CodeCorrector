public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Directly compute the cross product without preconditioning
    return new Vector3D(
        v1.y * v2.z - v1.z * v2.y,  // Component in i (x-component)
        v1.z * v2.x - v1.x * v2.z,  // Component in j (y-component)
        v1.x * v2.y - v1.y * v2.x   // Component in k (z-component)
    );
}
