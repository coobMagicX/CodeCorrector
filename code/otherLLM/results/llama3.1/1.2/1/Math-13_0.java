private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    
    // Check for numerical instability and large or ill-conditioned matrices
    if (dec.getConditionNumber() > 1e10) {
        // Handle numerical instability by using a more robust method, such as LDL decomposition
        return ldlDecomposition(m).getSquareRoot();
    }
    
    return dec.getSquareRoot();
}

// Method to perform LDL decomposition
private EigenDecomposition ldlDecomposition(RealMatrix m) {
    final LUDecomposition lu = new LUDecomposition(m);
    final RealMatrix diagonal = lu.getDiagonal();
    final RealMatrix lowerTriangular = lu.getLowerTriangle().transpose();
    
    return new EigenDecomposition(diagonal.multiply(lowerTriangular));
}