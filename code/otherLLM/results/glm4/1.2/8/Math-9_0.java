public Line revert() {
    final Line originalLine = this; // Store the reference to the original Line object
    final Vector3D reversedDirection = new Vector3D(-originalLine.getDirection().getX(), -originalLine.getDirection().getY(), -originalLine.getDirection().getZ());
    final Line reverted = new Line(zero, zero.subtract(reversedDirection));
    return reverted;
}