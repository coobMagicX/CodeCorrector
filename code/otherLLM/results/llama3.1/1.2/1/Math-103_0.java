import org.apache.commons.math3.special.Erf;

public class NormalDistribution {
    private double mean;
    private double standardDeviation;

    public NormalDistribution(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public NormalDistribution() {
        this(0, 1);
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        if (standardDeviation <= 0) {
            throw new IllegalArgumentException("Standard deviation must be positive.");
        }
        this.standardDeviation = standardDeviation;
    }

    public double cumulativeProbability(double x) throws MathException {
        return 0.5 * (1 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2))));
    }

    public double inverseCumulativeProbability(final double p) throws MathException {
        if (p == 0) {
            return Double.NEGATIVE_INFINITY;
        } else if (p == 1) {
            return Double.POSITIVE_INFINITY;
        }
        // Actual implementation of the inverse CDF for a normal distribution.
        // This is typically done using specialized libraries or algorithms,
        // and can be quite complex. For simplicity, let's assume we have one here:
        return mean + standardDeviation * Math.sqrt(2) * Erf.erfinv(2 * p - 1);
    }

    protected double getDomainLowerBound(double p) {
        return mean + standardDeviation * getQuantileFromErf(p);
    }

    protected double getDomainUpperBound(double p) {
        return mean + standardDeviation * getQuantileFromErf(1 - p);
    }

    protected double getInitialDomain(double p) {
        // This might be a simple implementation or something more complex.
        // For simplicity, let's just assume it's the same as the lower bound:
        return getDomainLowerBound(p);
    }

    private double getQuantileFromErf(double p) {
        // Implementing this is out of scope for this answer. It typically involves
        // using specialized libraries or algorithms.
        throw new UnsupportedOperationException("getQuantileFromErf not implemented.");
    }
}