public double[] repairAndDecode(final double[] x) {
    CMAESOptimizer optimizer = new CMAESOptimizer(100, null, 1000, 1e-6, true, 0, 0, new RandomGenerator(), false, new ConvergenceChecker<PointValuePair>() {
        @Override
        public boolean isConverged(PointValuePair pointValuePair) {
            return true; // This will always return true for this example.
        }
    });
    optimizer.optimize(); // Run the optimization process
    
    double[] minPoint = optimizer.getMinPoint();
    
    if (minPoint == null || minPoint.length != x.length) {
        throw new RuntimeException("Invalid minimum point found.");
    }
    
    double[] result = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        result[i] = Math.min(Math.max(x[i], 0.0), 1.0); // Ensure bounds are within [0, 1]
    }
    
    return decode(result);
}