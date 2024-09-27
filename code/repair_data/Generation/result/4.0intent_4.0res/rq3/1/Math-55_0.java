public class Vector3D {
    public final double x;
    public final double y;
    public final double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
        // Compute rho as an 8-bit approximation of v1.v2 / v2.v2
        double rho = dotProduct(v1, v2) / dotProduct(v2, v2);

        // Create v3 = v1 - rho * v2
        Vector3D v3 = new Vector3D(v1.x - rho * v2.x, v1.y - rho * v2.y, v1.z - rho * v2.z);

        // Compute the cross product from v3 and v2
        return new Vector3D(
            v3.y * v2.z - v3.z * v2.y,
            v3.z * v2.x - v3.x * v2.z,
            v3.x * v2.y - v3.y * v2.x
        );
    }
}