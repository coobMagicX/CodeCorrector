public Line revert() {
    // Calculate the reversed direction vector by multiplying the current direction with -1.
    final Vector revertedDirection = direction.multiply(-1);
    
    // Create a new Line instance with the same zero point and the calculated reversed direction.
    final Line reverted = new Line(zero, zero.add(revertedDirection));
    
    return reverted;
}