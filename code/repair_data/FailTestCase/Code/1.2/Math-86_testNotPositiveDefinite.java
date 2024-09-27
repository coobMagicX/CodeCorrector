    public void testNotPositiveDefinite() throws MathException {
        CholeskyDecomposition cd = new CholeskyDecompositionImpl(MatrixUtils.createRealMatrix(new double[][] {
                { 14, 11, 13, 15, 24 },
                { 11, 34, 13, 8,  25 },
                { 13, 13, 14, 15, 21 },
                { 15, 8,  15, 18, 23 },
                { 24, 25, 21, 23, 45 }
        }));
        System.out.println(cd.getL().multiply(cd.getLT()));
    }