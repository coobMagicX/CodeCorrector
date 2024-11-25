private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    return getWeightSquareRoot(); // Use the existing method to get the square root of the weights matrix
}