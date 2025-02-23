public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    double dotProduct = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    double normV2Squared = v2.x * v2.x + v2.y * v2.y + v2.z * v2.z;

    double rho = dotProduct / normV2Squared;
    
    // Compute v3 = v1 - rho * v2
    double v3x = v1.x - rho * v2.x;
    double v3y = v1.y - rho * v2.y;
    double v3z = v1.z - rho * v2.z;

    // Computing cross product of v3 and v2
    double cx = v3y * v2.z - v3z * v2.y;
    double cy = v3z * v2.x - v3x * v2.z;
    double cz = v3x * v2.y - v3y * v2.x;
    
    return new Vector3D(cx, cy, cz);
}
