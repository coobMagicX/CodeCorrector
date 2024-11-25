public static Vector3D crossProduct(final Vector3D v1, final Vector3D v2) {
    // Avoid division by zero when computing scaling factor rho
    if (v2.magnitude() == 0) {
        throw new IllegalArgumentException("Vector v2 cannot be a zero vector.");
    }

    double rho = 0.0;

    // Compute the scaling factor rho to minimize cancellation errors
    double dotProduct = v1.dot(v2);
    double denominator = v2.magnitude();
    double deltaRho = (dotProduct / denominator) * denominator;
    rho += deltaRho;

    // Compute the components of the cross product using the rescaled vectors
    double x = (v1.y - rho * v2.z) * v2.x;
    double y = (v1.z - rho * v2.x) * v2.y;
    double z = (v1.x - rho * v2.y) * v2.z;

    return new Vector3D(x, y, z);
}

// Assuming Vector3D class has the following methods:
class Vector3D {
    public final double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Method to compute the dot product of two vectors
    public double dot(final Vector3D v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    // Method to compute the magnitude (Euclidean norm) of a vector
    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
}