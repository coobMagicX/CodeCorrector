public static double inverseCumulativeProbability(NormalDistribution distribution, double probability) throws FunctionEvaluationException, ConvergenceException {
    if (probability < 0.0 || probability > 1.0) {
        throw MathRuntimeException.createIllegalArgumentException("Invalid probability: {0}", probability);
    }
    
    double lowerBound = -1e300;
    double upperBound = 1e300;
    int maximumIterations = 100;
    
    return inverseCumulativeProbability(distribution, probability, lowerBound, upperBound, maximumIterations);
}

public static double inverseCumulativeProbability(NormalDistribution distribution, double probability, 
        double lowerBound, double upperBound, int maximumIterations) throws ConvergenceException, FunctionEvaluationException {
    
    if (distribution == null) {
        throw MathRuntimeException.createIllegalArgumentException("distribution is null");
    }
    if (maximumIterations <= 0)  {
        throw MathRuntimeException.createIllegalArgumentException(
              "bad value for maximum iterations number: {0}", maximumIterations);
    }
    if (lowerBound >= upperBound) {
        throw MathRuntimeException.createIllegalArgumentException(
              "invalid range: lower bound={0}, upper bound={1}", lowerBound, upperBound);
    }
    
    UnivariateRealFunction function = new InverseCumulativeDistributionFunction(distribution);
    
    double[] bracketResult = bracket(function, 0.0, lowerBound, upperBound, maximumIterations);
    
    return function.value(bracketResult[1]);
}

private static class InverseCumulativeDistributionFunction implements UnivariateRealFunction {
    private final NormalDistribution distribution;
    
    public InverseCumulativeDistributionFunction(NormalDistribution distribution) {
        this.distribution = distribution;
    }
    
    @Override
    public double value(double x) throws FunctionEvaluationException {
        return distribution.inverseCumulativeProbability(x);
    }
}