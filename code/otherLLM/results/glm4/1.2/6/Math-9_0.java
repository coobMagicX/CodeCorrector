public Line revert() {
    final Line revertedLine = new Line(zero, direction);
    // Invert the direction vector by reversing its coordinates
    final double[] invertedDirection = new double[3];
    for (int i = 0; i < 3; i++) {
        invertedDirection[i] = -revertedLine.direction.get(i);
    }
    final Vector3D invertedDirVector = new Vector3D(invertedDirection);
    
    // Create a new Line with the reversed direction
    return new Line(zero, invertedDirVector);
}