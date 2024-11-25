public Line revert() {
    final Line reverted = new Line(zero, zero.add(direction.negate()));
    return reverted;
}