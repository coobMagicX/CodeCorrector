public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // rescale both vectors without losing precision,
    // to ensure their norm are the same order of magnitude
    double normV1 = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
    double normV2 = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
    double rho = (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z) / (normV2 * normV2);

    // we reduce cancellation errors by preconditioning,
    // we replace v1 by v3 = v1 - rho v2 with rho chosen in order to compute
    // v3 without loss of precision. See Kahan lecture
    // "Computing Cross-Products and Rotations in 2- and 3-Dimensional Euclidean Spaces"
    // available at http://www.cs.berkeley.edu/~wkahan/MathH110/Cross.pdf
    Vector3D v3 = new Vector3D(v1.x - rho * v2.x, v1.y - rho * v2.y, v1.z - rho * v2.z);

    // compute cross product from v3 and v2 instead of v1 and v2
    double x = v3.y * v2.z - v3.z * v2.y;
    double y = v3.z * v2.x - v3.x * v2.z;
    double z = v3.x * v2.y - v3.y * v2.x;

    // check for cancellation errors
    if (x == 0.0 && y == 0.0 && z == 0.0) {
        // compute cross product using higher precision arithmetic
        BigDecimal bdX = BigDecimal.valueOf(v3.y).multiply(BigDecimal.valueOf(v2.z)).subtract(BigDecimal.valueOf(v3.z).multiply(BigDecimal.valueOf(v2.y)));
        BigDecimal bdY = BigDecimal.valueOf(v3.z).multiply(BigDecimal.valueOf(v2.x)).subtract(BigDecimal.valueOf(v3.x).multiply(BigDecimal.valueOf(v2.z)));
        BigDecimal bdZ = BigDecimal.valueOf(v3.x).multiply(BigDecimal.valueOf(v2.y)).subtract(BigDecimal.valueOf(v3.y).multiply(BigDecimal.valueOf(v2.x)));

        return new Vector3D(bdX.doubleValue(), bdY.doubleValue(), bdZ.doubleValue());
    } else {
        return new Vector3D(x, y, z);
    }
}