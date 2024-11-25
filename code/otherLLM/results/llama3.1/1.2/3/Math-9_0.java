public Line revert() {
    final Vector3D invertedDirection = direction.negate();
    final Point3D zero = zero;
    final Point3D start = zero;
    final Point3D end = zero.add(invertedDirection);
    final Line reverted = new Line(start, end);
    return reverted;
}