public class Vector3D {
    private double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }
}

public class Line {
    private Vector3D origin;
    private Vector3D direction;

    public Line(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction;
    }

    // Existing methods for reference
    public Vector3D getOrigin() {
        return origin;
    }

    public Vector3D getDirection() {
        return direction;
    }

    // Repair the revert method
    public Line revert() {
        final Vector3D revertedDirection = new Vector3D(-direction.x, -direction.y, -direction.z);
        return new Line(origin, revertedDirection);
    }
}