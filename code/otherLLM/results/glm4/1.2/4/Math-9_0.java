public Line revert() {
    // Ensure direction is negated by creating a new Vector3D with negative coordinates
    final Vector3D negDirection = new Vector3D(-direction.x, -direction.y, -direction.z);
    final Line reverted = new Line(zero, negDirection);
    return reverted;
}