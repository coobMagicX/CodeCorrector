    public void testFullRank() {

        RealMatrix base = MatrixUtils.createRealMatrix(new double[][] {
            { 0.1159548705,      0.,           0.,           0.      },
            { 0.0896442724, 0.1223540781,      0.,           0.      },
            { 0.0852155322, 4.558668e-3,  0.1083577299,      0.      },
            { 0.0905486674, 0.0213768077, 0.0128878333, 0.1014155693 }
        });

        RealMatrix m = base.multiply(base.transpose());

        RectangularCholeskyDecomposition d =
                new RectangularCholeskyDecomposition(m, 1.0e-10);

        RealMatrix root = d.getRootMatrix();
        RealMatrix rebuiltM = root.multiply(root.transpose());
        Assert.assertEquals(0.0, m.subtract(rebuiltM).getNorm(), 1.0e-15);

        // the pivoted Cholesky decomposition is *not* unique. Here, the root is
        // not equal to the original trianbular base matrix
        Assert.assertTrue(root.subtract(base).getNorm() > 0.3);

    }