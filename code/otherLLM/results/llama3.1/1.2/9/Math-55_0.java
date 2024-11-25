public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    
    // compute rho as an 8 bits approximation of v1.v2 / v2.v2
    double rho = (v1.x * v2.y - v1.y * v2.x) / ((v2.x * v2.x + v2.y * v2.y + v2.z * v2.z));
    
    // compute cross product from v3 and v2 instead of v1 and v2
    double x = (rho * v2.x) + v1.x;
    double y = (rho * v2.y) + v1.y;
    double z = (rho * v2.z) + v1.z;
    
    return new Vector3D(y * v2.z - z * v2.y, z * v2.x - x * v2.z, x * v2.y - y * v2.x);
}