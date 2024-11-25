import org.apache.commons.math3.special.Erf;

public double cumulativeProbability(double x) {
    return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
}