private RealMatrix squareRoot(RealMatrix m) {
    if (m.isIdentity()) { // Check for identity matrix
        return m;
    }
    
    final EigenDecomposition dec = new EigenDecomposition(m);
    try {
        return dec.getSquareRoot();
    } catch (Exception e) {
        RealMatrix errorMatrix = MatrixUtil.computeErrorMatrix(dec, m); // Calculate and create an error matrix for assertion
        if (!MatrixAssert.isWithinThreshold(errorMatrix)) { // Assert the error matrix is within a certain threshold
            throw new Exception("Failed to compute square root: " + e.getMessage());
        }
        return dec.getSquareRoot(); // Return the computed square root even if error occurs
    }
}