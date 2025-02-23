public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Computing scalar 'rho' might not be possible due to method limitations.
    // Direct calculation of cross product as a fallback scenario:
    double v1Dotv2 = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z; // manually compute dot product of v1 and v2
    double v2Dotv2 = v2.x * v2.x + v2.y * v2.y + v2.z * v2.z; // manually compute dot product of v2 with itself

    double rho = v1Dotv2 / v2Dotv2;

    Vector3D scaledV2 = new Vector3D(v2.x * rho, v2.y * rho, v2.z * rho);

    // Compute v3 = v1 - rho * v2
    Vector3D v3 = new Vector3D(
        v1.x - scaledV2.x, 
        v1.y - scaledV2.y, 
        v1.z - scaledV2.z
    );

    // Now use v3 and v2 to compute the cross product
    return new Vector3D(
        v3.y * v2.z - v3.z * v2.y,  // Calculate the x component
        v3.z * v2.x - v3.x * v2.z,  // Calculate the y component
        v3.x * v2.y - v3.y * v2.x   // Calculate the z component
    );
}
