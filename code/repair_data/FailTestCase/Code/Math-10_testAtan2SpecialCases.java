    public void testAtan2SpecialCases() {

        DerivativeStructure pp =
                DerivativeStructure.atan2(new DerivativeStructure(2, 2, 1, +0.0),
                                          new DerivativeStructure(2, 2, 1, +0.0));
        Assert.assertEquals(0, pp.getValue(), 1.0e-15);
        Assert.assertEquals(+1, FastMath.copySign(1, pp.getValue()), 1.0e-15);

        DerivativeStructure pn =
                DerivativeStructure.atan2(new DerivativeStructure(2, 2, 1, +0.0),
                                          new DerivativeStructure(2, 2, 1, -0.0));
        Assert.assertEquals(FastMath.PI, pn.getValue(), 1.0e-15);

        DerivativeStructure np =
                DerivativeStructure.atan2(new DerivativeStructure(2, 2, 1, -0.0),
                                          new DerivativeStructure(2, 2, 1, +0.0));
        Assert.assertEquals(0, np.getValue(), 1.0e-15);
        Assert.assertEquals(-1, FastMath.copySign(1, np.getValue()), 1.0e-15);

        DerivativeStructure nn =
                DerivativeStructure.atan2(new DerivativeStructure(2, 2, 1, -0.0),
                                          new DerivativeStructure(2, 2, 1, -0.0));
        Assert.assertEquals(-FastMath.PI, nn.getValue(), 1.0e-15);

    }