private RealMatrix squareRoot(RealMatrix m) {
    // Check for singular matrix before attempting to compute the square root
    if (isSingular(m)) {
        throw new ArithmeticException("Cannot compute square root of a singular matrix.");
    }

    final EigenDecomposition dec = new EigenDecomposition(m);
    return dec.getSquareRoot();
}

private boolean isSingular(RealMatrix m) {
    // This method should check if the matrix is singular.
    // The implementation details depend on what exactly constitutes a "singular" matrix in this context.
    // For example, it could be checking if all eigenvalues are zero or very close to zero.

    EigenDecomposition dec = new EigenDecomposition(m);
    double[] eigenvalues = dec.getRealEigenvalues();
    for (double eigenvalue : eigenvalues) {
        if (Math.abs(eigenvalue) < 1e-8) { // Using a small threshold to account for floating point precision issues
            return true; // Matrix is singular if any eigenvalue is near zero.
        }
    }
    return false; // Matrix is not singular.
}