import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

private double calculateAsymptoticPValue(final double Umin,
                                         final int n1,
                                         final int n2)
        throws ConvergenceException, MaxCountExceededException {

    final int n1n2prod = n1 * n2;

    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

    // Correct calculation of z based on the normal distribution's standard deviation and mean
    final double z = (Umin - EU) / Math.sqrt(VarU);

    final NormalDistribution standardNormal = new NormalDistribution(0, 1);
    // Use the cumulative probability to find the area under the curve beyond Umin
    final double rightTailProbability = 1.0 - standardNormal.cumulativeProbability(z);

    return 2 * rightTailProbability;
}