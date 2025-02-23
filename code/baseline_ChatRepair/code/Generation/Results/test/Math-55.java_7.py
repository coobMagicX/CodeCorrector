public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double c1 = v1.y * v2.z - v1.z * v2.y;
    double c2 = v1.z * v2.x - v1.x * v2.z;
    double c3 = v1.x * v2.y - v1.y * v2.x;

    // Using Math.fma for better precision where available (Java 9+)
    // fma(a, b, c) performs a * b + c with only one rounding error
    return new Vector3D(
        Math.fma(v1.y, v2.z, -v1.z * v2.y),
        Math.fma(v1.z, v2.x, -v1.x * v2.z),
        Math.fma(v1.x, v2.y, -v1.y * v2.x)
    );
}
