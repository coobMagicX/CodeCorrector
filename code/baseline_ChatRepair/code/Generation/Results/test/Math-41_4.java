public double evaluate(final double[] values, final double[] weights,
                       final double mean, final int begin, int length) {

    double variance = Double.NaN;

    if (test(values, weights, begin, length)) {
        if (length == 1) {
            variance = 0.0;
        } else if (length > 1) {
            double accum = 0.0; 
            double accum2 = 0.0; 
            double sumWeights = 0.0; 

            for (int i = begin; i < begin + length; i++) {
                double deviation = values[i] - mean;
                double weight = weights[i];
                accum += weight * deviation * deviation;
                accum2 += weight * deviation;
                sumWeights += weight;
            }

            double meanDeviationSquared = (accum2 * accum2 / sumWeights);
            if (isBiasCorrected && sumWeights > 1.0) {
                variance = (accum - meanDeviationSquared) / (sumWeights - 1.0);
            } else {
                variance = (accum - meanDeviationSquared) / sumWeights;
            }
        }
    }

    return variance;
}
