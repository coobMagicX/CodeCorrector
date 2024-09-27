public Line revert() {
    final Vector3D negatedDirection = new Vector3D(-1, direction.getX(), -1, direction.getY(), -1, direction.getZ());
    final Line reverted = new Line(zero, negatedDirection);
    return reverted;
}