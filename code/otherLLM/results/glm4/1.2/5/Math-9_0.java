public Line revert() {
    final Vector oppositeDirection = negate(direction);
    final Line reverted = new Line(zero, zero.add(oppositeDirection));
    return reverted;
}

// Assuming there's a method to negate a vector:
private static Vector negate(Vector v) {
    // Negate the components of the vector
    return new Vector(-v.x, -v.y);
}