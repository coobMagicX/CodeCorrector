public Line revert() {
    final Point zeroCopy = new Point(zero.x, zero.y); // Create a copy of the original point
    final Point newDirection = direction.multiply(-1); // Multiply the direction by -1 to reverse it
    final Point reverted = new Point(zeroCopy.x + newDirection.x, zeroCopy.y + newDirection.y);
    return new Line(reverted, zeroCopy);
}