public class Line {
    private Vector2D direction;

    // Constructor for Line with a direction vector
    public Line(Vector2D direction) {
        this.direction = direction;
    }

    // Method to revert the line by negating its direction vector
    public Line revert() {
        final Vector2D reversedDirection = new Vector2D(-direction.getX(), -direction.getY());
        return new Line(reversedDirection);
    }

    // Helper method to get the direction vector
    public Vector2D getDirection() {
        return direction;
    }
}

// Assuming the existence of a Vector2D class with methods getX() and getY()
class Vector2D {
    private double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Subtract method for Vector2D
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    // Getter methods for x and y
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}