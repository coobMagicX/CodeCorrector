public Line revert() {
    final Vector zero = new Vector(0, 0); // Assuming 'zero' represents the zero vector.
    final Vector negativeDirection = direction.negate(); // Negate the direction vector.
    final Line reverted = new Line(zero, negativeDirection);
    return reverted;
}