public Line revert() {
    final Line reverted = new Line(zero, direction.negate());
    return reverted;
}
