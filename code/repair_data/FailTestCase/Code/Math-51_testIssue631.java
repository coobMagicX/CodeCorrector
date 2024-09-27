    public void testIssue631() {
        final UnivariateRealFunction f = new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.exp(x) - Math.pow(Math.PI, 3.0);
                }
            };

        final UnivariateRealSolver solver = new RegulaFalsiSolver();
        final double root = solver.solve(3624, f, 1, 10);
        Assert.assertEquals(3.4341896575482003, root, 1e-15);
	}