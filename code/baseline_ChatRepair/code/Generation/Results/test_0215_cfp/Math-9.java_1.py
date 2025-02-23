public Line revert() {
    final Line reverted = new Line(zero, direction.negate()); // Using a negate method to precisely handle direction inversion
    return reverted;
}
