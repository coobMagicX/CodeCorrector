import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.exception.MathException;

public class NormalDistribution {
    private double mean;
    private double standardDeviation;

    public NormalDistribution(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public double cumulativeProbability(double x) throws MathException {
        return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
    }
}