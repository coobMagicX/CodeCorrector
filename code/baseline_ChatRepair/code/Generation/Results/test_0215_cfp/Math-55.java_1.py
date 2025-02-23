public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    return new Vector3D(
        v1.y * v2.z - v1.z * v2.y,  // Calculate the x component
        v1.z * v2.x - v1.x * v2.z,  // Calculate the y component
        v1.x * v2.y - v1.y * v2.x   // Calculate the z component
    );
}
