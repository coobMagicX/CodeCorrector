public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Calculate the scaling factor (rho)
    double rho = 0.0;
    double numerator = v1.dot(v2);
    double denominator = v2.dot(v2);

    if (denominator != 0) {
        rho = numerator / denominator;

        // Rescale v2 to reduce precision loss
        Vector3D v2_scaled = v2.multiply(1.0 / Math.sqrt(denominator));

        // Compute the cross product using the rescaled v2
        return new Vector3D(v1.y * v2_scaled.z - v1.z * v2_scaled.y,
                            v1.z * v2_scaled.x - v1.x * v2_scaled.z,
                            v1.x * v2_scaled.y - v1.y * v2_scaled.x);
    } else {
        // If denominator is zero, the vectors are parallel and cross product is zero
        return new Vector3D(0.0, 0.0, 0.0);
    }
}

// Additional methods that might be needed:
public static double dot(final Vector3D v1, final Vector3D v2) {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
}

public static Vector3D multiply(final Vector3D v, double scalar) {
    return new Vector3D(v.x * scalar, v.y * scalar, v.z * scalar);
}