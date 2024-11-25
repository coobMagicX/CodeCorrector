public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Check if either vector is zero to avoid division by zero
    if (v1.isZero() || v2.isZero()) {
        throw new IllegalArgumentException("Input vectors cannot be zero.");
    }

    // Compute the scaling factor rho to reduce cancellation errors
    double rho = 0.0;
    double v1_dot_v2 = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    double v2_norm_squared = v2.x * v2.x + v2.y * v2.y + v2.z * v2.z;
    if (v2_norm_squared != 0.0) {
        // Compute rho using Kahan summation algorithm to minimize rounding errors
        double yh = 0.5 * v1_dot_v2 / v2_norm_squared;
        double rhoh = yh - Math.IEEEremainder(yh, 1.0);
        rho = rhoh > 0 ? Math.min(1.0e8, Math.max(1.0e-8, rhoh)) : Math.min(-1.0e8, Math.max(-1.0e-8, rhoh));
    }

    // Rescale v2 to have a norm of 1
    Vector3D v2_rescaled = new Vector3D(v2.x * (1 / v2_norm_squared), v2.y * (1 / v2_norm_squared), v2.z * (1 / v2_norm_squared));

    // Compute v3 as v1 - rho * v2
    Vector3D v3 = new Vector3D(v1.x - rho * v2_rescaled.x, v1.y - rho * v2_rescaled.y, v1.z - rho * v2_rescaled.z);

    // Compute the cross product from v3 and the rescaled v2
    return new Vector3D(v3.y * v2_rescaled.z - v3.z * v2_rescaled.y,
                        v3.z * v2_rescaled.x - v3.x * v2_rescaled.z,
                        v3.x * v2_rescaled.y - v3.y * v2_rescaled.x);
}