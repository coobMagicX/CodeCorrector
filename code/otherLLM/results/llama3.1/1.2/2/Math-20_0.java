public double[] repairAndDecode(final double[] x) {
    if (boundaries == null || inputSigma == null) {
        return decode(x);
    }
    CMAESOptimizer optimizer = new CMAESOptimizer(lambda, inputSigma,
            maxIterations, stopFitness, isActiveCMA, diagonalOnly,
            checkFeasableCount, random, generateStatistics, checker);
    double[] optimizedPoint = optimizer.optimize();
    
    // Validate the optimized point before decoding
    if (optimizedPoint == null || optimizedPoint.length != x.length) {
        throw new RuntimeException("Optimized point is invalid");
    }
    
    return decode(optimizedPoint);
}