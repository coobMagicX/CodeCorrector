public Line revert() {
    final Point z = new Point(0, 0);
    final Point oppositeDirection = direction.multiply(-1);
    final Point revertedCenter = zero.add(oppositeDirection);
    final Point[] reverted = {z, revertedCenter};
    return new Line(reverted);
}