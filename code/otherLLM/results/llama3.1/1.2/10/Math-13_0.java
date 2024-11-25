private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    // Check initialization and verify fitting process
    if (dec.getFittedValues().length != 2 * (m.getRowDimension() + m.getColumnDimension())) {
        throw new IllegalArgumentException("Invalid input matrix for square root calculation.");
    }
    
    // Refine error calculation
    double maxError = 0;
    for (int i = 0; i < m.getRowDimension(); i++) {
        RealMatrix row = m.getRow(i);
        RealMatrix squareRootRow = dec.getSquareRoot().getRow(i);
        for (int j = 0; j < row.getColumnDimension(); j++) {
            double error = Math.abs(row.getEntry(j, 0) - Math.pow(squareRootRow.getEntry(j, 0), 2));
            maxError = Math.max(maxError, error);
        }
    }

    // Verify the actual square root calculation
    for (int i = 0; i < m.getRowDimension(); i++) {
        RealMatrix row = dec.getSquareRoot().getRow(i);
        double sqrtValue = row.getEntry(0, 0);
        for (int j = 1; j <= m.getColumnDimension(); j++) {
            if (!isApproxEqual(sqrtValue, Math.pow(row.getEntry(j, 0), 2))) {
                throw new AssertionError("Square root calculation failed.");
            }
        }
    }

    return dec.getSquareRoot();
}

private boolean isApproxEqual(double a, double b) {
    return Math.abs(a - b) < 1e-9;
}