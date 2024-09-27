// New failed test
@Test
public void testSquareRootMatrix_InvalidMatrix() {
    double[][] data = {{1, 2}, {3, 4}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    RealMatrix sqrtMatrix = squareRoot(matrix);
    
    // Check if the result is NaN
    assertTrue(Double.isNaN(sqrtMatrix.getEntry(0, 0)));
    assertTrue(Double.isNaN(sqrtMatrix.getEntry(0, 1)));
    assertTrue(Double.isNaN(sqrtMatrix.getEntry(1, 0)));
    assertTrue(Double.isNaN(sqrtMatrix.getEntry(1, 1)));
}