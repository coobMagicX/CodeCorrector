public class Vector3D {
    private double x, y, z;

    // Constructor, setters, getters, and other methods are assumed to be defined here.

    public static Vector3D subtract(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
}

public class Line {
    private Vector3D origin;
    private Vector3D direction;

    // Constructor and other methods are assumed to be defined here.

    public Line(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Line revert() {
        final Vector3D revertedDirection = new Vector3D(0 - this.direction.x, 0 - this.direction.y, 0 - this.direction.z);
        return new Line(this.origin, revertedDirection);
    }
}