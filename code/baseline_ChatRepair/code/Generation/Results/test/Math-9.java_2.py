public Line revert() {
    final Line reverted = new Line(zero, zero.subtract(direction).normalize());
    return reverted;
}
