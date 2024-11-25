public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Ensure both vectors are normalized to avoid precision issues due to large magnitudes
    double normV1 = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    double normV2 = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);

    // Handle potential division by zero when the vectors are too small
    if (normV1 < 1e-16 || normV2 < 1e-16) {
        throw new ArithmeticException("Input vector is too small, causing division by a very small number.");
    }

    double rho = v1.dot(v2) / (normV2 * normV2);

    // Prevent numerical instability due to large rho
    if (Math.abs(rho) > 1.0) {
        throw new ArithmeticException("Numerical instability encountered with the computed rho value.");
    }

    Vector3D v3 = new Vector3D(v1.x - rho * v2.x, v1.y - rho * v2.y, v1.z - rho * v2.z);

    // Compute cross product from v3 and v2 instead of v1 and v2
    return new Vector3D(v3.y * v2.z - v3.z * v2.y, v3.z * v2.x - v3.x * v2.z, v3.x * v2.y - v3.y * v2.x);
}

// This method is assumed to be a member of the Vector3D class
public double dot(Vector3D other) {
    return this.x * other.x + this.y * other.y + this.z * other.z;
}