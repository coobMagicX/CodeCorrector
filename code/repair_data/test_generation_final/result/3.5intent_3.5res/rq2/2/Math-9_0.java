public Line revert() {
    final Line reverted = new Line(zero.subtract(direction), zero);
    return reverted;
}