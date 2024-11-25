public Line revert() {
    final Vector3D originalDirection = direction;
    final Vector3D revertedDirection = originalDirection.negate();
    final Line reverted = new Line(zero, revertedDirection);
    return reverted;
}