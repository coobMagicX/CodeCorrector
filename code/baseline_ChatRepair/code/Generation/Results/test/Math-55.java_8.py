public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double c1 = v1.y * v2.z - v1.z * v2.y;
    double c2 = v1.z * v2.x - v1.x * v2.z;
    double c3 = v1.x * v2.y - v1.y * v2.x;
    
    return new Vector3D(c1, c2, c3);
}
