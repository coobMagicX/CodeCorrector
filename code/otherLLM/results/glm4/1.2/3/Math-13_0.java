private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);

    // Ensure numerical stability in the calculation of the square root
    double[] eigenvalues = dec.getRealEigenvalues();
    double[] sqrtEigenvalues = new double[eigenvalues.length];
    
    for (int i = 0; i < eigenvalues.length; i++) {
        if (eigenvalues[i] < 0) {
            throw new IllegalArgumentException("Cannot compute the square root of a negative eigenvalue.");
        }
        sqrtEigenvalues[i] = Math.sqrt(eigenvalues[i]);
    }

    // Construct a diagonal matrix with the square roots of the eigenvalues
    double[][] diagMatData = new double[eigenvalues.length][eigenvalues.length];
    for (int i = 0; i < eigenvalues.length; i++) {
        diagMatData[i][i] = sqrtEigenvalues[i];
    }
    
    // Create a new RealMatrix with the square root diagonal matrix
    return MatrixUtils.createRealDiagonalMatrix(sqrtEigenvalues);
}