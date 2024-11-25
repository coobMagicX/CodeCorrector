public Line revert() {
    final Point zero = this.getZero();
    final Direction direction = this.getDirection();
    final Line reverted = new Line(zero, zero.add(direction.negate()));
    return reverted;
}