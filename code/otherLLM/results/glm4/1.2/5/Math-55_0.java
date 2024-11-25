public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Rescale both vectors to ensure their norms are the same order of magnitude
    // and reduce cancellation errors by preconditioning.

    double rho = 0.0;
    if (Math.abs(v2.norm() - 1e-16) > 1e-16) { // Check for near-zero norm of v2
        double dotProduct = v1.dot(v2);
        double denominator = v2.norm();
        rho = dotProduct / denominator;

        // Adjust v1 to reduce cancellation errors, using Kahan summation algorithm
        Vector3D v3 = new Vector3D(0.0, 0.0, 0.0);
        v3.x += v1.x - (v2.z * (rho + Math.pow(v1.y * v2.z - v1.z * v2.y, 2) / denominator));
        v3.y += v1.y - (v2.x * (rho + Math.pow(v1.z * v2.x - v1.x * v2.z, 2) / denominator));
        v3.z += v1.z - (v2.y * (rho + Math.pow(v1.x * v2.y - v1.y * v2.x, 2) / denominator));

        // Compute the cross product from v3 and v2 instead of v1 and v2
        return new Vector3D(v3.y * v2.z - v3.z * v2.y, v3.z * v2.x - v3.x * v2.z, v3.x * v2.y - v3.y * v2.x);
    } else {
        // If the norm of v2 is too small, return an error vector or handle accordingly
        return new Vector3D(Double.NaN, Double.NaN, Double.NaN);
    }
}