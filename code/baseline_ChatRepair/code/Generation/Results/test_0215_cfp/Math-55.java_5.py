public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double cx = v1.y * v2.z - v1.z * v2.y;
    double cy = v1.z * v2.x - v1.x * v2.z;
    double cz = v1.x * v2.y - v1.y * v2.x;

    return new Vector3D(cx, cy, cz);
}
