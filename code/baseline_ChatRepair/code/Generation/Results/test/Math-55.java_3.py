public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // First normalize both vectors to reduce loss of precision.
    Vector3D v1Normalized = v1.normalize();
    Vector3D v2Normalized = v2.normalize();

    // Compute scalar product (dot product) and the square of the norm of v2
    double dot = v1Normalized.dotProduct(v2Normalized);
    double v2SquaredNorm = v2Normalized.getNormSq();

    // Compute rho as an approximation of v1.v2 / v2.v2
    double rho = dot / v2SquaredNorm;
    
    // Compute v3 as v1 - rho * v2 to reduce precision errors
    Vector3D v3 = v1Normalized.subtract(v2Normalized.scalarMultiply(rho));
    
    // Now compute cross product from v3 and v2
    return new Vector3D(
        v3.y * v2Normalized.z - v3.z * v2Normalized.y,
        v3.z * v2Normalized.x - v3.x * v2Normalized.z,
        v3.x * v2Normalized.y - v3.y * v2Normalized.x
    );
}
