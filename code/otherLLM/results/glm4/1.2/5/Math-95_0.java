import org.apache.commons.math3.stat.inference.ChiSquareTest;

protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    
    // Use the ChiSquareTest to calculate the chi-square critical value, which corresponds to the inverse cumulative probability.
    ret = new ChiSquareTest().chiSquareCritical(d - 2, 1 - p);
    
    return ret;
}