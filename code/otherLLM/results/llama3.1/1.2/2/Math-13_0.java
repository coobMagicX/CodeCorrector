private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    
    // Perform parameter validation to ensure the input 'm' is valid for Eigen Decomposition
    if (dec.getActualEigenvalues().length != 1) {
        throw new NotANonPositiveDefiniteMatrixException("Input matrix must be positive definite");
    }

    return dec.getSquareRoot();
}