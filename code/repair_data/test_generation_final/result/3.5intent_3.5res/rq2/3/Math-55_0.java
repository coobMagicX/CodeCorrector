public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double v1Norm = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    double v2Norm = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
    double v1RescaledX = v1.x * v2Norm / v1Norm;
    double v1RescaledY = v1.y * v2Norm / v1Norm;
    double v1RescaledZ = v1.z * v2Norm / v1Norm;
    
    // compute rho as an 8 bits approximation of v1.v2 / v2.v2
    double rho = (v1RescaledX * v2RescaledX + v1RescaledY * v2RescaledY + v1RescaledZ * v2RescaledZ) / (v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
    
    // compute v3 as v1 - rho * v2
    double v3X = v1RescaledX - rho * v2.x;
    double v3Y = v1RescaledY - rho * v2.y;
    double v3Z = v1RescaledZ - rho * v2.z;
    
    // compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(v3Y * v2.z - v3Z * v2.y, v3Z * v2.x - v3X * v2.z, v3X * v2.y - v3Y * v2.x);
}